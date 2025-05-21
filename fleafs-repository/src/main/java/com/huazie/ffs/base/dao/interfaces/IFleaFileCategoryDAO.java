package com.huazie.ffs.base.dao.interfaces;

import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;

import java.util.List;

/**
 * Flea文件类目DAO层接口
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileCategoryDAO extends IAbstractFleaJPADAO<FleaFileCategory> {

    /**
     * 获取Flea文件类目集
     *
     * @param categoryCode 类目编号
     * @param categoryName 类目名称
     * @param parentId 父类目编号
     * @return Flea文件类目集
     * @throws CommonException 通用异常
     */
    List<FleaFileCategory> queryFleaFileCategorys(String categoryCode, String categoryName, Long parentId) throws CommonException;
}