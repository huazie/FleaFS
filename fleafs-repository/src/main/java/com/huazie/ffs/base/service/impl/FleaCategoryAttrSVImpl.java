package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaCategoryAttrDAO;
import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.ffs.base.service.interfaces.IFleaCategoryAttrSV;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Flea类目属性SV层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaCategoryAttrSV")
public class FleaCategoryAttrSVImpl extends AbstractFleaJPASVImpl<FleaCategoryAttr> implements IFleaCategoryAttrSV {

    private IFleaCategoryAttrDAO fleaCategoryAttrDao;

    @Autowired
    @Qualifier("fleaCategoryAttrDAO")
    public void setFleaCategoryAttrDao(IFleaCategoryAttrDAO fleaCategoryAttrDao) {
        this.fleaCategoryAttrDao = fleaCategoryAttrDao;
    }

    @Override
    protected IAbstractFleaJPADAO<FleaCategoryAttr> getDAO() {
        return fleaCategoryAttrDao;
    }
}