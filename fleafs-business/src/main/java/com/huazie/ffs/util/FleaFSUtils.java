package com.huazie.ffs.util;

import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.fleaframework.common.FleaApplicationContext;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.core.base.cfgdata.bean.FleaConfigDataSpringBean;
import com.huazie.fleaframework.core.base.cfgdata.entity.FleaConfigData;

/**
 * FleaFS工具类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFSUtils {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFSUtils.class);

    private static volatile FleaConfigDataSpringBean configBean;

    private static FleaConfigDataSpringBean getConfigBean() {
        if (ObjectUtils.isEmpty(configBean)) {
            synchronized (FleaFSUtils.class) {
                if (ObjectUtils.isEmpty(configBean)) {
                    try {
                        configBean = FleaApplicationContext.getBean(FleaConfigDataSpringBean.class);
                    } catch (Exception e) {
                        LOGGER.error1(new Object(){}, "初始化FleaConfigDataSpringBean失败", e);
                    }
                }
            }
        }
        return configBean;
    }

    private FleaFSUtils() {
    }

    /**
     * 获取 Token 失效的分钟数
     *
     * @return 失效的分钟数
     * @since 1.0.0
     */
    public static int getTokenExpMinutes() {
        // 默认Token 5分钟后失效
        int minutes = 5;
        try {
            // Flea Config 配置数据Bean
            FleaConfigDataSpringBean configBean = getConfigBean();
            if (ObjectUtils.isNotEmpty(configBean)) {
                // 从配置中获取 Token 失效相关的配置数据
                FleaConfigData configData = configBean.getConfigData(FleaFSConstants.ConfigDataConstants.CONFIG_TYPE_TOKEN_EXPIRY_CONFIG,
                        FleaFSConstants.ConfigDataConstants.CONFIG_TYPE_TOKEN_EXPIRY_CONFIG);
                if (ObjectUtils.isNotEmpty(configData)) {
                    minutes = Integer.parseInt(configData.getData1());
                }
            }
        } catch (CommonException e) {
            LOGGER.error1(new Object(){}, "获取Token失效配置异常", e);
        }
        return minutes;
    }
}
