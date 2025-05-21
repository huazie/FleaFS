package com.huazie.ffs.category;

import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
import com.huazie.fleaframework.common.exceptions.CommonException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * FleaFS文件类目单元测试类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FleaFSCategoryTest {

    @Autowired
    @Qualifier("fleaFileCategorySV")
    private IFleaFileCategorySV fleaFileCategorySV;

    @Test
    public void testInsertCategory() throws CommonException {
        FleaFileCategory fleaFileCategory = new FleaFileCategory(1000L, "test", "测试类目", "1111110000000000", null);
        fleaFileCategorySV.save(fleaFileCategory);
    }
}
