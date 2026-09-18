package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaCategoryAttrDAO;
import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.ffs.base.service.interfaces.IFleaCategoryAttrSV;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public String queryCategoryAttrValue(Long categoryId, String attrCode) throws CommonException {
        List<FleaCategoryAttr> fleaCategoryAttrs = this.fleaCategoryAttrDao.queryValidFleaCategoryAttrs(categoryId, attrCode);
        FleaCategoryAttr categoryAttr = CollectionUtils.getFirstElement(fleaCategoryAttrs, FleaCategoryAttr.class);
        if (ObjectUtils.isNotEmpty(categoryAttr) && StringUtils.isNotBlank(categoryAttr.getAttrValue())) {
            return categoryAttr.getAttrValue();
        }
        return null;
    }

    @Override
    protected IAbstractFleaJPADAO<FleaCategoryAttr> getDAO() {
        return fleaCategoryAttrDao;
    }
}