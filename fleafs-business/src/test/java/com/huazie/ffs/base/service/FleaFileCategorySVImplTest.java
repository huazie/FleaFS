package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
import com.huazie.ffs.base.service.interfaces.IFleaTokenInfoSV;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.fleaframework.common.EncryptionAlgorithmEnum;
import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.exception.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.RandomCode;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FleaFileCategorySVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileCategorySVImplTest.class);

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LOGGER.debug("ApplicationContext={}", applicationContext);
    }

    @Test
    public void insertFleaFileVersion() {
        try {
            IFleaFileCategorySV fleaFileCategorySV = (IFleaFileCategorySV) applicationContext.getBean("fleaFileCategorySV");
            FleaFileCategory fleaFileCategory = new FleaFileCategory();
            fleaFileCategory.setCategoryId(10000L);
            fleaFileCategory.setCategoryCode("Code");
            fleaFileCategory.setCategoryName("编码");
            fleaFileCategory.setParentId(-1L);
            fleaFileCategory.setEncryptType(EncryptionAlgorithmEnum.AES.getAlgorithm());
            fleaFileCategory.setMaxFileSize(100L);
            fleaFileCategory.setOperationState("1111110000000000");
            fleaFileCategory.setState(EntityStateEnum.IN_USE.getState());
            fleaFileCategory.setCreateDate(DateUtils.getCurrentTime());
            fleaFileCategory.setRemarks("测试类目");

            fleaFileCategorySV.save(fleaFileCategory);

        } catch (CommonException e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void queryFileVersion() throws CommonException {
        IFleaFileCategorySV fleaFileCategorySV = (IFleaFileCategorySV) applicationContext.getBean("fleaFileCategorySV");
        FleaFileCategory fleaFileCategory = new FleaFileCategory();
        fleaFileCategory.setCategoryCode("Code");
        fleaFileCategory.setCategoryName("编码");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("categoryCode");
        attrNames.add("categoryName");
        List<FleaFileCategory> fleaTokenInfoList =  fleaFileCategorySV.query(attrNames, fleaFileCategory);
        LOGGER.debug("FleaFileCategorys = {}", fleaTokenInfoList);
    }

}