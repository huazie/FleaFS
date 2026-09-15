package com.huazie.ffs.base.service.interfaces;

import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.service.interfaces.IAbstractFleaJPASV;

import java.util.List;

/**
 * Flea文件版本SV层接口定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileVersionSV extends IAbstractFleaJPASV<FleaFileVersion> {

    /**
     * 保存文件版本记录，留存文件快照
     * <p> 版本编码按文件已有版本数自动递增生成，如 V1、V2；
     * <p> 文件版本记录用于文件历史版本查询与回溯。
     *
     * @param fileInfo    文件信息（作为版本快照的数据来源）
     * @param versionDesc 版本描述
     * @return 文件版本记录
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    FleaFileVersion saveFleaFileVersion(FleaFileInfo fileInfo, String versionDesc) throws CommonException;

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