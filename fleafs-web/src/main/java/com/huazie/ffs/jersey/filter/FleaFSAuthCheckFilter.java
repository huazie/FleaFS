package com.huazie.ffs.jersey.filter;

import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.common.exceptions.ServiceException;
import com.huazie.fleaframework.jersey.common.data.FleaJerseyRequest;
import com.huazie.fleaframework.jersey.common.data.FleaJerseyResponse;
import com.huazie.fleaframework.jersey.common.data.RequestPublicData;
import com.huazie.fleaframework.jersey.server.filter.IFleaJerseyFilter;

/**
 * FleaFS业务授权校验过滤器
 *
 * <p> 系统账户、操作账户、资源授权校验已由框架内置的授权校验过滤器处理，
 * 此处仅校验请求资源是否为FleaFS支持的业务操作（upload、download、update、delete、search、version）。
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

        // 获取请求资源编码【如 upload、download 等，对应FleaFS文件管理操作】
        RequestPublicData requestPublicData = request.getRequestData().getPublicData();
        String resourceCode = requestPublicData.getResourceCode();

        // 校验请求资源是否为FleaFS支持的业务操作
        OperateTypeEnum operateTypeEnum = getOperateType(resourceCode);
        if (operateTypeEnum == null) {
            // ERROR-SERVICE0000000008 未授权的FleaFS业务资源【{0}】，请检查！
            throw new ServiceException("ERROR-SERVICE0000000008", resourceCode);
        }

        LOGGER.debug1(obj, "FleaFS Auth Check, 业务操作 = {}", operateTypeEnum.getName());
        LOGGER.debug1(obj, "FleaFS Auth Check, End");
    }

    /**
     * 根据资源编码获取对应的FleaFS文件管理操作类型
     *
     * @param resourceCode 资源编码
     * @return FleaFS文件管理操作类型，未匹配时返回null
     */
    private OperateTypeEnum getOperateType(String resourceCode) {
        if (StringUtils.isBlank(resourceCode)) return null;
        for (OperateTypeEnum operateTypeEnum : OperateTypeEnum.values()) {
            if (operateTypeEnum.getType().equalsIgnoreCase(resourceCode)) {
                return operateTypeEnum;
            }
        }
        return null;
    }
}
