package com.huazie.ffs.base.category;

import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.ffs.base.service.interfaces.IFleaCategoryAttrSV;
import com.huazie.ffs.base.util.FleaFSEntityUtils;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFSCategoryAttr {

    private IFleaCategoryAttrSV fleaCategoryAttrSV;

    public FleaFSCategoryAttr(IFleaCategoryAttrSV fleaCategoryAttrSV) {
        this.fleaCategoryAttrSV = fleaCategoryAttrSV;
    }

    public void addAuthCheckModeAttr(Long categoryId) throws CommonException {
        String authCheckMode = "3";
        FleaCategoryAttr fleaCategoryAttr = FleaFSEntityUtils.newAuthCheckModeAttr(categoryId, null, authCheckMode);
        fleaCategoryAttrSV.save(fleaCategoryAttr);
        FleaCategoryAttr fleaCategoryAttr1 = FleaFSEntityUtils.newAuthCheckModeAttr(categoryId, "1", authCheckMode);
        fleaCategoryAttrSV.save(fleaCategoryAttr1);
        FleaCategoryAttr fleaCategoryAttr2 = FleaFSEntityUtils.newAuthCheckModeAttr(categoryId, "2", authCheckMode);
        fleaCategoryAttrSV.save(fleaCategoryAttr2);
        FleaCategoryAttr fleaCategoryAttr3 = FleaFSEntityUtils.newAuthCheckModeAttr(categoryId, "3", authCheckMode);
        fleaCategoryAttrSV.save(fleaCategoryAttr3);
        FleaCategoryAttr fleaCategoryAttr4 = FleaFSEntityUtils.newAuthCheckModeAttr(categoryId, "4", authCheckMode);
        fleaCategoryAttrSV.save(fleaCategoryAttr4);
        FleaCategoryAttr fleaCategoryAttr5 = FleaFSEntityUtils.newAuthCheckModeAttr(categoryId, "5", authCheckMode);
        fleaCategoryAttrSV.save(fleaCategoryAttr5);
        FleaCategoryAttr fleaCategoryAttr6 = FleaFSEntityUtils.newAuthCheckModeAttr(categoryId, "6", authCheckMode);
        fleaCategoryAttrSV.save(fleaCategoryAttr6);
    }

    public void addLimitSystemUsersAttr(Long categoryId) throws CommonException {

    }

    public void addLimitOperationUsersAttr(Long categoryId) throws CommonException {

    }
}
