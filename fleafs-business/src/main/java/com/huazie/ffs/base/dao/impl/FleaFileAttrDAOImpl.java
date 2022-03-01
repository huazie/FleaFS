package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileAttrDAO;
import com.huazie.ffs.base.entity.FleaFileAttr;
import org.springframework.stereotype.Repository;

/**
 * Flea文件属性DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaFileAttrDAO")
public class FleaFileAttrDAOImpl extends FleaFSDAOImpl<FleaFileAttr> implements IFleaFileAttrDAO {
}