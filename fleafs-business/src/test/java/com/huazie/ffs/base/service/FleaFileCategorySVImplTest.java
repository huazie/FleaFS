package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
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
 * 已验证
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FleaFileCategorySVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileCategorySVImplTest.class);

    @Resource(name = "fleaFileCategorySV")
    private IFleaFileCategorySV fleaFileCategorySV;

    @Test
    public void insertFleaFileCategory() throws CommonException {
        String categoryCode = "test1";
        String categoryName = "测试类目1";
        String operationState = "1111110000000000";
        String remarks = "这是用于测试类目，包含已知所有的文件管理操作权限";
        FleaFileCategory fleaFileCategory = new FleaFileCategory(1000L, categoryCode, categoryName, operationState, remarks);
        fleaFileCategorySV.save(fleaFileCategory);
    }

    @Test
    public void queryFileCategory() throws CommonException {
        FleaFileCategory fleaFileCategory = new FleaFileCategory();
        fleaFileCategory.setCategoryCode("test");
        fleaFileCategory.setCategoryName("测试类目");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("categoryCode");
        attrNames.add("categoryName");
        List<FleaFileCategory> fleaTokenInfoList = fleaFileCategorySV.query(attrNames, fleaFileCategory);
        LOGGER.debug("FleaFileCategory List = {}", fleaTokenInfoList);
    }

    @Test
    public void deleteFileCategory() throws CommonException {
        LOGGER.debug("Flag = {}", fleaFileCategorySV.remove(1000L));
    }

}