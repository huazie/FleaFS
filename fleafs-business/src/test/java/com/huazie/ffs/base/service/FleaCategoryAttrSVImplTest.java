package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.ffs.base.entity.FleaFileAttr;
import com.huazie.ffs.base.service.interfaces.IFleaCategoryAttrSV;
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
public class FleaCategoryAttrSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaCategoryAttrSVImplTest.class);

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LOGGER.debug("ApplicationContext={}", applicationContext);
    }

    @Test
    public void insertFleaCategoryAttr() {
        try {
            IFleaCategoryAttrSV fleaCategoryAttrSV = (IFleaCategoryAttrSV) applicationContext.getBean("fleaCategoryAttrSV");
            FleaCategoryAttr fleaCategoryAttr = new FleaCategoryAttr();
            fleaCategoryAttr.setCategoryId(10000L);
            fleaCategoryAttr.setAttrCode("code_desc");
            fleaCategoryAttr.setAttrDesc("测试");
            fleaCategoryAttr.setCreateDate(DateUtils.getCurrentTime());
            fleaCategoryAttr.setEffectiveDate(DateUtils.getCurrentTime());
            fleaCategoryAttr.setExpiryDate(DateUtils.getExpiryTimeForever());
            fleaCategoryAttr.setState(1);

            fleaCategoryAttrSV.save(fleaCategoryAttr);
        } catch (CommonException e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void queryFleaCategoryAttr() throws CommonException {
        IFleaCategoryAttrSV fleaCategoryAttrSV = (IFleaCategoryAttrSV) applicationContext.getBean("fleaCategoryAttrSV");
        FleaCategoryAttr fleaCategoryAttr = new FleaCategoryAttr();
        fleaCategoryAttr.setAttrCode("code_desc");
        fleaCategoryAttr.setAttrDesc("测试");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("fileId");
        List<FleaCategoryAttr> fleaCategoryAttrs =  fleaCategoryAttrSV.query(attrNames, fleaCategoryAttr);
        LOGGER.debug("FleaCategoryAttrs = {}", fleaCategoryAttrs);
    }
}