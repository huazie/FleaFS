package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.dao.interfaces.IFleaCategoryAttrDAO;
import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.DateUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Flea类目属性DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaCategoryAttrDAO")
public class FleaCategoryAttrDAOImpl extends FleaFSDAOImpl<FleaCategoryAttr> implements IFleaCategoryAttrDAO {

    @Override
    public List<FleaCategoryAttr> queryValidFleaCategoryAttrs(Long categoryId, String attrCode) throws CommonException {
        Date currentDate = DateUtils.getCurrentTime();
        return this.getQuery(null)
                .equal(FleaFSEntityConstants.FileCategoryEntityConstants.E_CATEGORY_ID, categoryId)
                .equal(FleaFSEntityConstants.E_ATTR_CODE, attrCode)
                .equal(FleaFSEntityConstants.E_STATE, EntityStateEnum.IN_USE.getState())
                .lessThan(FleaFSEntityConstants.E_EFFECTIVE_DATE, currentDate)
                .greaterThan(FleaFSEntityConstants.E_EXPIRY_DATE, currentDate).getResultList();
    }
}