package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaTokenInfoDAO;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.base.service.interfaces.IFleaTokenInfoSV;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    protected IAbstractFleaJPADAO<FleaTokenInfo> getDAO() {
        return fleaTokenInfoDao;
    }
}