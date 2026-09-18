package com.huazie.ffs.jersey.filter;

import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.ffs.module.auth.service.interfaces.IFleaFSAuthSV;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.common.exceptions.ServiceException;
import com.huazie.fleaframework.jersey.common.data.FleaJerseyRequest;
import com.huazie.fleaframework.jersey.common.data.FleaJerseyResponse;
import com.huazie.fleaframework.jersey.common.data.RequestBusinessData;
import com.huazie.fleaframework.jersey.common.data.RequestPublicData;
import com.huazie.fleaframework.jersey.server.filter.IFleaJerseyFilter;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * FleaFS业务授权校验过滤器
 *
 * <p> 位于框架内置的授权校验过滤器【AuthCheckFilter】之后，完成 FleaFS 文件管理层面的校验，
 * 分三步进行：
 * <ol>
 *     <li>校验请求资源是否为 FleaFS 支持的业务操作（upload、download、update、delete、search、version），
 *         非 FleaFS 业务资源直接拦截；</li>
 *     <li>校验文件类目是否启用该文件管理操作【类目操作状态 operation_state 的对应位】；</li>
 *     <li>依据文件类目配置的授权校验方式【AUTH_CHECK_MODE】，完成系统用户、操作用户授权校验。</li>
 * </ol>
 *
 * <p> 其中，系统账户、操作账户以及账户对资源的授权校验已由框架内置的授权校验过滤器处理；
 * 第 2、3 步的落地入口为 {@link com.huazie.ffs.module.auth.service.interfaces.IFleaFSAuthSV}，
 * 具体规则详见 {@link com.huazie.ffs.util.FleaFSAuthCheck}。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFSAuthCheckFilter implements IFleaJerseyFilter {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFSAuthCheckFilter.class);

    @Override
    public void doFilter(FleaJerseyRequest request, FleaJerseyResponse response) throws CommonException {
        Object obj = new Object() {};
        LOGGER.debug1(obj, "FleaFS Auth Check, Start");

        // 请求公共报文
        RequestPublicData requestPublicData = request.getRequestData().getPublicData();
        // 获取请求资源编码【如 upload、download 等，对应FleaFS文件管理操作】
        String resourceCode = requestPublicData.getResourceCode();
        // 获取服务编码
        String serviceCode = requestPublicData.getServiceCode();

        // 校验请求资源是否为FleaFS支持的业务操作
        OperateTypeEnum operateTypeEnum = OperateTypeEnum.getOperateType(resourceCode);
        if (ObjectUtils.isEmpty(operateTypeEnum)) {
            // ERROR-SERVICE0000000008 未授权的FleaFS业务资源【{0}】，请检查！
            throw new ServiceException("ERROR-SERVICE0000000008", resourceCode);
        }

        LOGGER.debug1(obj, "FleaFS Auth Check, 业务操作 = {}", operateTypeEnum.getName());

        // 获取Web应用上下文对象
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        if (ObjectUtils.isEmpty(webApplicationContext)) {
            LOGGER.debug1(obj, "FleaFS Auth Check, WebApplicationContext 为空, 跳过文件管理授权校验");
            return;
        }

        // 系统账户编号
        Long systemAccountId = getAccountId(requestPublicData.getSystemAccountId());
        // 操作账户编号
        Long accountId = getAccountId(requestPublicData.getAccountId());

        // 请求业务报文
        RequestBusinessData requestBusinessData = request.getRequestData().getBusinessData();
        String inputJson = ObjectUtils.isEmpty(requestBusinessData) ? null : requestBusinessData.getInput();

        // 获取FleaFS文件管理授权服务
        IFleaFSAuthSV fleaFSAuthSV = webApplicationContext.getBean(IFleaFSAuthSV.class);
        // 文件管理授权校验
        fleaFSAuthSV.checkFileAuth(resourceCode, serviceCode, inputJson, systemAccountId, accountId);

        LOGGER.debug1(obj, "FleaFS Auth Check, End");
    }

    /**
     * 获取请求公共报文中的账户编号
     *
     * @param accountId 账户编号字符串
     * @return 账户编号，非法时返回null
     * @since 1.0.0
     */
    private Long getAccountId(String accountId) {
        if (StringUtils.isBlank(accountId)) return null;
        try {
            return Long.valueOf(StringUtils.trim(accountId));
        } catch (NumberFormatException e) {
            LOGGER.error1(new Object() {}, "FleaFS Auth Check, 账户编号非法, accountId = {}", accountId);
            return null;
        }
    }
}
