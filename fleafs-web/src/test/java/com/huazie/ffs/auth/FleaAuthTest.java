package com.huazie.ffs.auth;

import com.huazie.ffs.auth.operation.FleaFSFileOperation;
import com.huazie.ffs.auth.operation.FleaFSFileOperationAttr;
import com.huazie.fleaframework.auth.common.service.interfaces.IFleaFunctionModuleSV;
import com.huazie.fleaframework.common.exceptions.CommonException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FleaAuthTest {

    @Resource(name = "fleaFunctionModuleSV")
    private IFleaFunctionModuleSV fleaFunctionModuleSV;

    @Test
    public void saveFleaFSFileOperation() throws CommonException {
        FleaFSFileOperation fleaFSFileOperation = new FleaFSFileOperation(fleaFunctionModuleSV);
        fleaFSFileOperation.addFleaOperation();
    }

    @Test
    public void saveFleaFSFileOperationAttr() throws CommonException {
        FleaFSFileOperationAttr fleaFSFileOperationAttr = new FleaFSFileOperationAttr(fleaFunctionModuleSV);
        fleaFSFileOperationAttr.addFleaOperationAttr();
    }
}
