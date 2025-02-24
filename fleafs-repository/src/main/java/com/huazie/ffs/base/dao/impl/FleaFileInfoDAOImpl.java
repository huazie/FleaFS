package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileInfoDAO;
import com.huazie.ffs.base.entity.FleaFileInfo;
import org.springframework.stereotype.Repository;

/**
 * Flea文件信息DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaFileInfoDAO")
public class FleaFileInfoDAOImpl extends FleaFSDAOImpl<FleaFileInfo> implements IFleaFileInfoDAO {
}