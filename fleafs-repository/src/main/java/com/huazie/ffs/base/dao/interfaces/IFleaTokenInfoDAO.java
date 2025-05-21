package com.huazie.ffs.base.dao.interfaces;

import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;

/**
 * Flea鉴权信息DAO层接口
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaTokenInfoDAO extends IAbstractFleaJPADAO<FleaTokenInfo> {

    /**
     * 查询有效的Flea鉴权信息
     *
     * @param token 鉴权token
     * @return Flea鉴权信息
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    FleaTokenInfo queryValidFleaTokenInfo(String token) throws CommonException;
}