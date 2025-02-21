package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileCategoryDAO;
import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    protected IAbstractFleaJPADAO<FleaFileCategory> getDAO() {
        return fleaFileCategoryDao;
    }
}