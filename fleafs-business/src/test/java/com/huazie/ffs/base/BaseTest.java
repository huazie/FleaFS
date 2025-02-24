package com.huazie.ffs.base;

import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.core.base.cfgdata.bean.FleaConfigDataSpringBean;
import com.huazie.fleaframework.core.base.cfgdata.entity.FleaConfigData;
import com.huazie.fleaframework.core.base.cfgdata.service.interfaces.IFleaConfigDataSV;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author huazie
 * @version 1.1.0
 * @since 1.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BaseTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(BaseTest.class);

    @Resource(name = "fleaConfigDataSV")
    private IFleaConfigDataSV fleaConfigDataSV;

    @Resource
    private FleaConfigDataSpringBean fleaConfigDataSpringBean;

    @Test
    public void getConfigDataById() throws CommonException {
        FleaConfigData fleaConfigData = fleaConfigDataSV.query(1L);
        LOGGER.debug("FleaConfigData = {}", fleaConfigData);
    }

    @Test
    public void getConfigData() throws CommonException {
        fleaConfigDataSV.getConfigData("huazie", "huazie");
    }

    @Test
    public void testConfigData() throws CommonException {
        fleaConfigDataSpringBean.getConfigData("huazie", "huazie");
    }
}
