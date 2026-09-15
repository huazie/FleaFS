package com.huazie.ffs.base.dao.interfaces;

import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;

import java.util.List;

/**
 * Flea文件版本DAO层接口
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileVersionDAO extends IAbstractFleaJPADAO<FleaFileVersion> {

    /**
     * 查询指定文件的所有有效版本记录，按版本编号倒序排列
     *
     * @param fileId 文件编号
     * @return 文件版本记录集合
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    List<FleaFileVersion> queryFleaFileVersions(String fileId) throws CommonException;

    /**
     * 查询指定文件的最新版本记录
     *
     * @param fileId 文件编号
     * @return 最新文件版本记录
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    FleaFileVersion queryLastFleaFileVersion(String fileId) throws CommonException;

}