package com.huazie.ffs.jersey.filter;

import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.jersey.common.data.FleaJerseyRequest;
import com.huazie.fleaframework.jersey.common.data.FleaJerseyResponse;
import com.huazie.fleaframework.jersey.server.filter.IFleaJerseyFilter;

/**
 * FleaFS业务授权校验过滤器
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

        LOGGER.debug1(obj,"FleaFS Auth Check, End");
    }
}
