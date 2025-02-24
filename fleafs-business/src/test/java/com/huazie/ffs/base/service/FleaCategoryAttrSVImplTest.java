package com.huazie.ffs.base.service;

import com.huazie.ffs.base.category.FleaFSCategoryAttr;
import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.ffs.base.service.interfaces.IFleaCategoryAttrSV;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
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
public class FleaCategoryAttrSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaCategoryAttrSVImplTest.class);

    @Resource(name = "fleaCategoryAttrSV")
    private IFleaCategoryAttrSV fleaCategoryAttrSV;

    @Test
    public void insertAuthCheckModeAttr() throws CommonException {
        FleaFSCategoryAttr fleaFSCategoryAttr = new FleaFSCategoryAttr(fleaCategoryAttrSV);
        fleaFSCategoryAttr.addAuthCheckModeAttr(1000L);
    }

    @Test
    public void insertLimitSystemUsersAttr() throws CommonException {
        FleaFSCategoryAttr fleaFSCategoryAttr = new FleaFSCategoryAttr(fleaCategoryAttrSV);
        fleaFSCategoryAttr.addLimitSystemUsersAttr(1000L);
    }

    @Test
    public void insertLimitOperationUsersAttr() throws CommonException {
        FleaFSCategoryAttr fleaFSCategoryAttr = new FleaFSCategoryAttr(fleaCategoryAttrSV);
        fleaFSCategoryAttr.addLimitOperationUsersAttr(1000L);
    }

    @Test
    public void queryFleaCategoryAttr() throws CommonException {
        FleaCategoryAttr fleaCategoryAttr = new FleaCategoryAttr();
        fleaCategoryAttr.setAttrCode("code_desc");
        fleaCategoryAttr.setAttrDesc("测试");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("fileId");
        List<FleaCategoryAttr> fleaCategoryAttrs =  fleaCategoryAttrSV.query(attrNames, fleaCategoryAttr);
        LOGGER.debug("FleaCategoryAttrs = {}", fleaCategoryAttrs);
    }

    @Test
    public void deleteFleaCategoryAttr() throws CommonException {
        for (long i = 0L; i <= 7L; i++) {
            fleaCategoryAttrSV.remove(i + 1);
        }
    }
}