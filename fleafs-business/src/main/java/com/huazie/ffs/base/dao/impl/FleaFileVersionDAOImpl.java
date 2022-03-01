package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileVersionDAO;
import com.huazie.ffs.base.entity.FleaFileVersion;
import org.springframework.stereotype.Repository;

/**
 * Flea文件版本DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaFileVersionDAO")
public class FleaFileVersionDAOImpl extends FleaFSDAOImpl<FleaFileVersion> implements IFleaFileVersionDAO {
}