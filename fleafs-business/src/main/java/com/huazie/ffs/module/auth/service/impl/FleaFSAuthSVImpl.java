package com.huazie.ffs.module.auth.service.impl;

import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
import com.huazie.ffs.base.util.FleaFSCheck;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.ffs.module.auth.common.IFleaFileCategoryLocator;
import com.huazie.ffs.module.auth.service.interfaces.IFleaFSAuthSV;
import com.huazie.ffs.util.FleaFSAuthCheck;
import com.huazie.fleaframework.common.FleaApplicationContext;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.ReflectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.common.util.json.GsonUtils;
import com.huazie.fleaframework.core.base.cfgdata.bean.FleaConfigDataSpringBean;
import com.huazie.fleaframework.core.base.cfgdata.entity.FleaJerseyResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FleaFS 文件管理授权服务实现类
 *
 * <p> 请求业务报文中并不直接携带文件类目编号，各文件管理服务的入参结构也不相同，
 * 因此具体的定位方式由各文件管理服务的实现类通过 {@link IFleaFileCategoryLocator}
 * 按文件管理操作类型自行定制【文件类目编号、文件编号或鉴权令牌】，本类只负责分派与编排。
 * 未能定位到文件类目【如文件搜索服务】时，不做类目级校验。
 *
 * <p> 定位到文件类目后，依次完成两步类目级校验：
 * <ol>
 *     <li>操作启用校验：类目操作状态【operation_state】对应位须启用当前文件管理操作</li>
 *     <li>授权校验：按类目配置的授权校验方式【AUTH_CHECK_MODE】校验系统用户、操作用户</li>
 * </ol>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaFSAuthSVImpl implements IFleaFSAuthSV {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFSAuthSVImpl.class);

    /**
     * 文件类目定位器集【以文件管理操作类型为键，由各文件管理服务的实现类自行提供】
     */
    private final Map<OperateTypeEnum, IFleaFileCategoryLocator> categoryLocatorMap = new HashMap<>();

    private IFleaFileCategorySV fleaFileCategorySV;

    @Autowired
    @Qualifier("fleaFileCategorySV")
    public void setFleaFileCategorySV(IFleaFileCategorySV fleaFileCategorySV) {
        this.fleaFileCategorySV = fleaFileCategorySV;
    }

    @Autowired(required = false)
    public void setCategoryLocators(List<IFleaFileCategoryLocator> categoryLocators) {
        if (CollectionUtils.isEmpty(categoryLocators)) return;

        for (IFleaFileCategoryLocator categoryLocator : categoryLocators) {
            OperateTypeEnum operateTypeEnum = categoryLocator.getOperateType();
            if (ObjectUtils.isEmpty(operateTypeEnum)) continue;
            this.categoryLocatorMap.put(operateTypeEnum, categoryLocator);
        }
    }

    @Override
    public void checkFileAuth(String resourceCode, String serviceCode, String inputJson, Long systemAccountId, Long accountId) throws CommonException {
        Object obj = new Object() {};
        LOGGER.debug1(obj, "FleaFS File Auth Check, resourceCode = {}, serviceCode = {}", resourceCode, serviceCode);

        // 获取FleaFS文件管理操作类型
        OperateTypeEnum operateTypeEnum = OperateTypeEnum.getOperateType(resourceCode);
        if (ObjectUtils.isEmpty(operateTypeEnum)) {
            LOGGER.debug1(obj, "FleaFS File Auth Check, operateType is invalid, resourceCode = {}", resourceCode);
            return;
        }

        // 获取文件类目编号【定位方式由各文件管理服务自行定制】
        Long categoryId = getCategoryId(operateTypeEnum, resourceCode, serviceCode, inputJson);
        if (ObjectUtils.isEmpty(categoryId)) {
            LOGGER.debug1(obj, "FleaFS File Auth Check, categoryId is empty, skip");
            return;
        }

        // 校验文件类目是否存在，以及是否启用当前文件管理操作
        checkOperationState(categoryId, operateTypeEnum);

        // 文件管理授权校验
        FleaFSAuthCheck.checkFileAuth(categoryId, operateTypeEnum, systemAccountId, accountId);
    }

    /**
     * 校验文件类目是否启用指定的文件管理操作
     *
     * <p> 类目操作状态【operation_state】按位对应各个文件管理操作，对应位为 1 表示启用；
     * 未配置操作状态时，默认所有操作均启用。
     *
     * @param categoryId      文件类目编号
     * @param operateTypeEnum 文件管理操作类型
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private void checkOperationState(Long categoryId, OperateTypeEnum operateTypeEnum) throws CommonException {
        Object obj = new Object() {};
        LOGGER.debug1(obj, "FleaFS File Auth Check, checkOperationState, categoryId = {}, operateType = {}",
                categoryId, ObjectUtils.isEmpty(operateTypeEnum) ? null : operateTypeEnum.getName());

        // 获取Flea文件类目
        FleaFileCategory fleaFileCategory = fleaFileCategorySV.queryFleaCategory(categoryId, null);
        // 校验文件类目是否存在
        FleaFSCheck.checkFleaFileCategory(fleaFileCategory, categoryId, null);
        // 校验类目是否启用该文件管理操作
        FleaFSCheck.checkOperationState(fleaFileCategory, operateTypeEnum, categoryId);
    }

    /**
     * 获取请求业务入参对应的文件类目编号
     *
     * <p> 先按文件管理操作类型取到对应的文件类目定位器，再由定位器从业务入参中定位文件类目编号；
     * 未提供定位器的文件管理操作【如文件搜索】，直接返回 null。
     *
     * @param operateTypeEnum 文件管理操作类型
     * @param resourceCode    资源编码
     * @param serviceCode     服务编码
     * @param inputJson       请求业务报文JSON串
     * @return 文件类目编号，未能定位时返回null
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private Long getCategoryId(OperateTypeEnum operateTypeEnum, String resourceCode, String serviceCode, String inputJson) throws CommonException {
        if (StringUtils.isBlank(inputJson)) return null;

        // 获取文件类目定位器
        IFleaFileCategoryLocator categoryLocator = categoryLocatorMap.get(operateTypeEnum);
        if (ObjectUtils.isEmpty(categoryLocator)) {
            LOGGER.debug1(new Object() {}, "FleaFS File Auth Check, category locator not found, operateType = {}", operateTypeEnum.getName());
            return null;
        }

        // 解析请求业务入参
        Object inputObj = parseInputObject(resourceCode, serviceCode, inputJson);
        if (ObjectUtils.isEmpty(inputObj)) return null;

        // 定位文件类目编号
        return categoryLocator.getCategoryId(inputObj);
    }

    /**
     * 解析请求业务报文中业务入参对象
     *
     * @param resourceCode 资源编码
     * @param serviceCode  服务编码
     * @param inputJson    请求业务报文JSON串
     * @return 业务入参对象
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private Object parseInputObject(String resourceCode, String serviceCode, String inputJson) throws CommonException {
        // Flea Config 配置数据Bean
        FleaConfigDataSpringBean fleaConfigDataSpringBean = FleaApplicationContext.getBean(FleaConfigDataSpringBean.class);
        if (ObjectUtils.isEmpty(fleaConfigDataSpringBean)) return null;

        // 根据资源编码 和 服务编码 获取 资源服务配置数据
        FleaJerseyResService resService = fleaConfigDataSpringBean.getResService(resourceCode, serviceCode);
        if (ObjectUtils.isEmpty(resService) || StringUtils.isBlank(resService.getServiceInput())) return null;

        Class<?> inputClazz = ReflectUtils.forName(resService.getServiceInput());
        if (ObjectUtils.isEmpty(inputClazz)) return null;

        return GsonUtils.toEntity(inputJson, inputClazz);
    }
}
