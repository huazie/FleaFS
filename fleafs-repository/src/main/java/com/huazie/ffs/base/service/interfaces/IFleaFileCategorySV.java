package com.huazie.ffs.base.service.interfaces;

import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.service.interfaces.IAbstractFleaJPASV;

/**
 * Flea文件类目SV层接口定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileCategorySV extends IAbstractFleaJPASV<FleaFileCategory> {

    /**
     * 获取Flea文件类目数据
     *
     * @param categoryId   类目编号（两者必传一个）
     * @param categoryCode 类目编码（两者必传一个）
     * @return Flea文件类目数据
     * @since 1.0.0
     */
    FleaFileCategory queryFleaCategory(Long categoryId, String categoryCode) throws CommonException;
}