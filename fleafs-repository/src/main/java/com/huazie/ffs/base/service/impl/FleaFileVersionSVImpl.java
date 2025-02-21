package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileVersionDAO;
import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.ffs.base.service.interfaces.IFleaFileVersionSV;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Flea文件版本SV层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaFileVersionSV")
public class FleaFileVersionSVImpl extends AbstractFleaJPASVImpl<FleaFileVersion> implements IFleaFileVersionSV {

    private IFleaFileVersionDAO fleaFileVersionDao;

    @Autowired
    @Qualifier("fleaFileVersionDAO")
    public void setFleaFileVersionDao(IFleaFileVersionDAO fleaFileVersionDao) {
        this.fleaFileVersionDao = fleaFileVersionDao;
    }

    @Override
    protected IAbstractFleaJPADAO<FleaFileVersion> getDAO() {
        return fleaFileVersionDao;
    }
}