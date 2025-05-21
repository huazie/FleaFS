package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.dao.interfaces.IFleaFileCategoryDAO;
import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.fleaframework.common.exceptions.CommonException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Flea文件类目DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaFileCategoryDAO")
public class FleaFileCategoryDAOImpl extends FleaFSDAOImpl<FleaFileCategory> implements IFleaFileCategoryDAO {

    @Override
    public List<FleaFileCategory> queryFleaFileCategorys(String categoryCode, String categoryName, Long parentId) throws CommonException {
        return this.getQuery(null)
                .equal(FleaFSEntityConstants.FileCategoryEntityConstants.E_CATEGORY_CODE, categoryCode)
                .like(FleaFSEntityConstants.FileCategoryEntityConstants.E_CATEGORY_NAME, categoryName) // 模糊匹配
                .equal(FleaFSEntityConstants.FileCategoryEntityConstants.E_PARENT_ID, parentId)
                .getResultList();
    }
}