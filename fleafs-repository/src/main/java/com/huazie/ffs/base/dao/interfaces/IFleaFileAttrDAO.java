package com.huazie.ffs.base.dao.interfaces;

import com.huazie.ffs.base.entity.FleaFileAttr;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;

import java.util.List;

/**
 * Flea文件属性DAO层接口
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileAttrDAO extends IAbstractFleaJPADAO<FleaFileAttr> {

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