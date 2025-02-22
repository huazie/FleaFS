package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileInfoDAO;
import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.service.interfaces.IFleaFileInfoSV;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Flea文件信息SV层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaFileInfoSV")
public class FleaFileInfoSVImpl extends AbstractFleaJPASVImpl<FleaFileInfo> implements IFleaFileInfoSV {

    private IFleaFileInfoDAO fleaFileInfoDao;

    @Autowired
    @Qualifier("fleaFileInfoDAO")
    public void setFleaFileInfoDao(IFleaFileInfoDAO fleaFileInfoDao) {
        this.fleaFileInfoDao = fleaFileInfoDao;
    }

    @Override
    protected IAbstractFleaJPADAO<FleaFileInfo> getDAO() {
        return fleaFileInfoDao;
    }
}