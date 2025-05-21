package com.huazie.ffs.base.service.interfaces;

import com.huazie.ffs.base.entity.FleaFileAttr;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.service.interfaces.IAbstractFleaJPASV;

import java.util.List;
import java.util.Map;

/**
 * Flea文件属性SV层接口定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileAttrSV extends IAbstractFleaJPASV<FleaFileAttr> {

    /**
     * 预生成文件关联类目的文件属性信息
     *
     * @param fileId     文件编号
     * @param categoryId 类目编号
     * @param extendMap  扩展Map
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    void preSavFileRelCategoryAttr(String fileId, Long categoryId, Map<String, Object> extendMap) throws CommonException;

    /**
     * 查询有效的Flea文件属性信息列表
     *
     * @param fileId   文件编号
     * @param attrCode 文件属性编码
     * @return Flea文件属性信息列表
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    List<FleaFileAttr> queryValidFleaFileAttrs(String fileId, String attrCode) throws CommonException;
}