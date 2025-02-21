package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaFileAttr;
import com.huazie.ffs.base.service.interfaces.IFleaFileAttrSV;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FleaFileAttrSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileAttrSVImplTest.class);

    @Resource(name = "fleaFileAttrSV")
    private IFleaFileAttrSV fleaFileAttrSV;

    @Test
    public void insertFleaFileAttr() throws CommonException {
        FleaFileAttr fleaFileAttr = new FleaFileAttr();
        fleaFileAttr.setFileId("E120EA280AA50693D5568D0071456400");
        fleaFileAttr.setAttrCode("test");
        fleaFileAttr.setAttrCode("test");
        fleaFileAttr.setAttrDesc("测试");
        fleaFileAttr.setCreateDate(DateUtils.getCurrentTime());
        fleaFileAttr.setEffectiveDate(DateUtils.getCurrentTime());
        fleaFileAttr.setExpiryDate(DateUtils.getExpiryTimeForever());
        fleaFileAttr.setState(1);

        fleaFileAttrSV.save(fleaFileAttr);
    }

    @Test
    public void queryFileAttr() throws CommonException {
        FleaFileAttr fleaFileAttr = new FleaFileAttr();
        fleaFileAttr.setFileId("E120EA280AA50693D5568D0071456400");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("fileId");
        List<FleaFileAttr> fleaFileAttrs =  fleaFileAttrSV.query(attrNames, fleaFileAttr);
        LOGGER.debug("FleaFileAttrs = {}", fleaFileAttrs);
    }
}