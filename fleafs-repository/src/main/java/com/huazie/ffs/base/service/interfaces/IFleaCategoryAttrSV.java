package com.huazie.ffs.base.service.interfaces;

import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.service.interfaces.IAbstractFleaJPASV;

/**
 * Flea类目属性SV层接口定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaCategoryAttrSV extends IAbstractFleaJPASV<FleaCategoryAttr> {

    /**
     * 查询指定类目下有效的属性值
     *
     * @param categoryId 类目编号
     * @param attrCode   类目属性编码
     * @return 类目属性值，未配置时返回 null
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    String queryCategoryAttrValue(Long categoryId, String attrCode) throws CommonException;
}