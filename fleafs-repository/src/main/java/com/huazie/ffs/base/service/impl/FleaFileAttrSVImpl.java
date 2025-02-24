package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileAttrDAO;
import com.huazie.ffs.base.entity.FleaFileAttr;
import com.huazie.ffs.base.service.interfaces.IFleaFileAttrSV;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Flea文件属性SV层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaFileAttrSV")
public class FleaFileAttrSVImpl extends AbstractFleaJPASVImpl<FleaFileAttr> implements IFleaFileAttrSV {

    private IFleaFileAttrDAO fleaFileAttrDao;

    @Autowired
    @Qualifier("fleaFileAttrDAO")
    public void setFleaFileAttrDao(IFleaFileAttrDAO fleaFileAttrDao) {
        this.fleaFileAttrDao = fleaFileAttrDao;
    }

    @Override
    protected IAbstractFleaJPADAO<FleaFileAttr> getDAO() {
        return fleaFileAttrDao;
    }
}