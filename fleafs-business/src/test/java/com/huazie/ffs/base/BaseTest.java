package com.huazie.ffs.base;

import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.core.base.cfgdata.entity.FleaConfigData;
import com.huazie.fleaframework.core.base.cfgdata.service.interfaces.IFleaConfigDataSV;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author huazie
 * @version 1.1.0
 * @since 1.1.0
 */
public class BaseTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(BaseTest.class);

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LOGGER.debug("ApplicationContext={}", applicationContext);
    }

    @Test
    public void getConfigDataById() throws Exception {
        IFleaConfigDataSV sv = (IFleaConfigDataSV) applicationContext.getBean("fleaConfigDataSV");

        FleaConfigData fleaConfigData = sv.query(1L);
        LOGGER.debug("FleaConfigData = {}", fleaConfigData);
    }

    @Test
    public void getConfigData() {
        IFleaConfigDataSV sv = (IFleaConfigDataSV) applicationContext.getBean("fleaConfigDataSV");
        try {
            sv.getConfigData("huazie", "huazie");
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
        }
    }
}
