package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileCategoryDAO;
import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.common.util.ExceptionUtils;
import com.huazie.fleaframework.common.util.NumberUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.common.exceptions.ServiceException;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.huazie.ffs.base.FleaFSEntityConstants.FileCategoryEntityConstants;

/**
 * Flea文件类目SV层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaFileCategorySV")
public class FleaFileCategorySVImpl extends AbstractFleaJPASVImpl<FleaFileCategory> implements IFleaFileCategorySV {

    private IFleaFileCategoryDAO fleaFileCategoryDao;

    @Autowired
    @Qualifier("fleaFileCategoryDAO")
    public void setFleaFileCategoryDao(IFleaFileCategoryDAO fleaFileCategoryDao) {
        this.fleaFileCategoryDao = fleaFileCategoryDao;
    }

    @Override
    public FleaFileCategory queryFleaCategory(Long categoryId, String categoryCode) throws CommonException {
        if (NumberUtils.isPositiveNumber(categoryId)) {
            return fleaFileCategoryDao.query(categoryId);
        } else if (StringUtils.isNotBlank(categoryCode)) {
            List<FleaFileCategory> fleaFileCategoryList = fleaFileCategoryDao.queryFleaFileCategorys(categoryCode, null, -1L);
            return CollectionUtils.getFirstElement(fleaFileCategoryList, FleaFileCategory.class);
        } else {
            // ERROR-SERVICE0000000002 入参【{0}】或【{1}】两者必填一个，请检查！
            ExceptionUtils.throwCommonException(ServiceException.class, "ERROR-SERVICE0000000002",
                    FileCategoryEntityConstants.E_CATEGORY_ID, FileCategoryEntityConstants.E_CATEGORY_CODE);
        }
        return null;
    }

    @Override
    protected IAbstractFleaJPADAO<FleaFileCategory> getDAO() {
        return fleaFileCategoryDao;
    }
}