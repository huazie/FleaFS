package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaFileAttr;
import com.huazie.ffs.base.service.interfaces.IFleaFileAttrSV;
import com.huazie.fleaframework.common.exception.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFileAttrSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileAttrSVImplTest.class);

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LOGGER.debug("ApplicationContext={}", applicationContext);
    }

    @Test
    public void insertFleaFileAttr() {
        try {
            IFleaFileAttrSV fleaFileInfoSV = (IFleaFileAttrSV) applicationContext.getBean("fleaFileAttrSV");
            FleaFileAttr fleaFileAttr = new FleaFileAttr();
            fleaFileAttr.setFileId("E120EA280AA50693D5568D0071456400");
            fleaFileAttr.setAttrCode("test");
            fleaFileAttr.setAttrCode("test");
            fleaFileAttr.setAttrDesc("测试");
            fleaFileAttr.setCreateDate(DateUtils.getCurrentTime());
            fleaFileAttr.setEffectiveDate(DateUtils.getCurrentTime());
            fleaFileAttr.setExpiryDate(DateUtils.getExpiryTimeForever());
            fleaFileAttr.setState(1);

            fleaFileInfoSV.save(fleaFileAttr);
        } catch (CommonException e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void queryFileAttr() throws CommonException {
        IFleaFileAttrSV fleaFileAttrSV = (IFleaFileAttrSV) applicationContext.getBean("fleaFileAttrSV");
        FleaFileAttr fleaFileAttr = new FleaFileAttr();
        fleaFileAttr.setFileId("E120EA280AA50693D5568D0071456400");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("fileId");
        List<FleaFileAttr> fleaFileAttrs =  fleaFileAttrSV.query(attrNames, fleaFileAttr);
        LOGGER.debug("FleaFileAttrs = {}", fleaFileAttrs);
    }
}