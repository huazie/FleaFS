package com.huazie.ffs.base.service.interfaces;

import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.service.interfaces.IAbstractFleaJPASV;

import java.util.Map;

/**
 * Flea文件信息SV层接口定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileInfoSV extends IAbstractFleaJPASV<FleaFileInfo> {

    /**
     * 预生成一条文件信息记录
     *
     * @param token     鉴权token
     * @param fileName  文件名
     * @param extendMap 扩展Map
     * @return 文件编号
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    String preSaveFleaFileInfo(String token, String fileName, Map<String, Object> extendMap) throws CommonException;

    /**
     * 生成文件编号，其由 当前时间 + UUID 组成
     *
     * @return 文件编号
     * @since 1.0.0
     */
    String generateFileId();

    //void updateFleaFileInfo(String fileId, String fileName) throws CommonException;
}