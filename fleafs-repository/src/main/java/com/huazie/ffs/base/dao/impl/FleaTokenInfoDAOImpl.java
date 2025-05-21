package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.dao.interfaces.IFleaTokenInfoDAO;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.common.util.DateUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Flea鉴权信息DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaTokenInfoDAO")
public class FleaTokenInfoDAOImpl extends FleaFSDAOImpl<FleaTokenInfo> implements IFleaTokenInfoDAO {

    @Override
    public FleaTokenInfo queryValidFleaTokenInfo(String token) throws CommonException {
        Date currentDate = DateUtils.getCurrentTime();

        List<FleaTokenInfo> fleaTokenInfos = this.getQuery(null).initQueryEntity(new FleaTokenInfo(token))
                .equal(FleaFSEntityConstants.TokenInfoEntityConstants.E_TOKEN_ID, token)
                .equal(FleaFSEntityConstants.E_STATE, EntityStateEnum.IN_USE.getState())
                .lessThan(FleaFSEntityConstants.E_EFFECTIVE_DATE, currentDate)
                .greaterThan(FleaFSEntityConstants.E_EXPIRY_DATE, currentDate).getResultList();

        return CollectionUtils.getFirstElement(fleaTokenInfos, FleaTokenInfo.class);
    }
}