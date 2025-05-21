package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileAttrDAO;
import com.huazie.ffs.base.entity.FleaFileAttr;
import com.huazie.ffs.base.service.interfaces.IFleaFileAttrSV;
import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public void preSavFileRelCategoryAttr(String fileId, Long categoryId, Map<String, Object> extendMap) throws CommonException {
        FleaFileAttr fleaFileAttr = new FleaFileAttr(fileId, FleaFSConstants.AttrConstants.ATTR_CODE_CATEGORY_ID,
                StringUtils.valueOf(categoryId), "", null);
        this.fleaFileAttrDao.save(fleaFileAttr);
    }

    @Override
    public List<FleaFileAttr> queryValidFleaFileAttrs(String fileId, String attrCode) throws CommonException {
        return this.fleaFileAttrDao.queryValidFleaFileAttrs(fileId, attrCode);
    }

    @Override
    protected IAbstractFleaJPADAO<FleaFileAttr> getDAO() {
        return fleaFileAttrDao;
    }
}