package com.huazie.ffs.base.dao.interfaces;

import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;

import java.util.List;

/**
 * Flea类目属性DAO层接口
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaCategoryAttrDAO extends IAbstractFleaJPADAO<FleaCategoryAttr> {

    /**
     * 查询有效的Flea类目属性信息列表
     *
     * @param categoryId 类目编号
     * @param attrCode   类目属性编码
     * @return Flea类目属性信息列表
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    List<FleaCategoryAttr> queryValidFleaCategoryAttrs(Long categoryId, String attrCode) throws CommonException;
}