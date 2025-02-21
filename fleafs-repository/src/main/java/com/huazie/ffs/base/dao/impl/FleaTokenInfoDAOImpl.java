package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaTokenInfoDAO;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import org.springframework.stereotype.Repository;

/**
 * Flea鉴权信息DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaTokenInfoDAO")
public class FleaTokenInfoDAOImpl extends FleaFSDAOImpl<FleaTokenInfo> implements IFleaTokenInfoDAO {
}