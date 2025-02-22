package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileCategoryDAO;
import com.huazie.ffs.base.entity.FleaFileCategory;
import org.springframework.stereotype.Repository;

/**
 * Flea文件类目DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaFileCategoryDAO")
public class FleaFileCategoryDAOImpl extends FleaFSDAOImpl<FleaFileCategory> implements IFleaFileCategoryDAO {
}