package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.dao.interfaces.IFleaTokenInfoDAO;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.base.service.interfaces.IFleaTokenInfoSV;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.MapUtils;
import com.huazie.fleaframework.common.util.RandomCode;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Flea鉴权信息SV层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaTokenInfoSV")
public class FleaTokenInfoSVImpl extends AbstractFleaJPASVImpl<FleaTokenInfo> implements IFleaTokenInfoSV {

    private IFleaTokenInfoDAO fleaTokenInfoDao;

    @Autowired
    @Qualifier("fleaTokenInfoDAO")
    public void setFleaTokenInfoDao(IFleaTokenInfoDAO fleaTokenInfoDao) {
        this.fleaTokenInfoDao = fleaTokenInfoDao;
    }

    @Override
    public String generateToken() {
        return RandomCode.toUUID();
    }

    @Override
    public void saveFleaTokenInfo(String token, String fileId, Long categoryId, OperateTypeEnum operateTypeEnum, Map<String, Object> extendMap) throws CommonException {
        Date expiryDate = (Date) MapUtils.getObject(extendMap, FleaFSEntityConstants.E_EXPIRY_DATE);
        FleaTokenInfo fleaTokenInfo = new FleaTokenInfo(token, fileId, categoryId, operateTypeEnum.getType(), null, expiryDate, "");
        this.save(fleaTokenInfo);
    }

    @Override
    public FleaTokenInfo queryValidFleaTokenInfo(String token) throws CommonException {
        return this.fleaTokenInfoDao.queryValidFleaTokenInfo(token);
    }

    @Override
    protected IAbstractFleaJPADAO<FleaTokenInfo> getDAO() {
        return fleaTokenInfoDao;
    }
}