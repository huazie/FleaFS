package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.dao.interfaces.IFleaFileAttrDAO;
import com.huazie.ffs.base.entity.FleaFileAttr;
import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.DateUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Flea文件属性DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaFileAttrDAO")
public class FleaFileAttrDAOImpl extends FleaFSDAOImpl<FleaFileAttr> implements IFleaFileAttrDAO {

    @Override
    public List<FleaFileAttr> queryValidFleaFileAttrs(String fileId, String attrCode) throws CommonException {
        Date currentDate = DateUtils.getCurrentTime();
        return this.getQuery(null)
                .equal(FleaFSEntityConstants.FileInfoEntityConstants.E_FILE_ID, fileId)
                .equal(FleaFSEntityConstants.E_ATTR_CODE, attrCode)
                .equal(FleaFSEntityConstants.E_STATE, EntityStateEnum.IN_USE.getState())
                .lessThan(FleaFSEntityConstants.E_EFFECTIVE_DATE, currentDate)
                .greaterThan(FleaFSEntityConstants.E_EXPIRY_DATE, currentDate).getResultList();
    }
}