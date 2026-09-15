package com.huazie.ffs.module.delete.service.interfaces;

import com.huazie.ffs.pojo.delete.input.InputDeleteAuthInfo;
import com.huazie.ffs.pojo.delete.input.InputFileDeleteInfo;
import com.huazie.ffs.pojo.delete.output.OutputDeleteAuthInfo;
import com.huazie.ffs.pojo.delete.output.OutputFileDeleteInfo;
import com.huazie.fleaframework.common.exceptions.CommonException;

/**
 * Flea删除服务接口，提供删除鉴权、文件删除的功能
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaDeleteSV {

    /**
     * 删除授权
     *
     * @param input 删除授权业务入参
     * @return 删除授权业务出参
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    OutputDeleteAuthInfo deleteAuth(InputDeleteAuthInfo input) throws CommonException;

    /**
     * 文件删除【逻辑删除】
     *
     * @param input 文件删除入参（包含删除鉴权令牌）
     * @return 文件删除出参（包含文件编号）
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    OutputFileDeleteInfo fileDelete(InputFileDeleteInfo input) throws CommonException;

    /**
     * 文件物理删除【内部管理接口，供管理端定时任务调用】
     * <p> 针对已逻辑删除的文件：删除FastDFS中的文件，清空文件信息中的存储信息（fastdfs_id、secret_key），
     * 文件记录保留用于审计。
     * <p> 逻辑删除文件默认保留 {@code FleaFSConstants.DeleteConstants#LOGIC_DELETE_KEEP_DAYS} 天，
     * 管理端可定期按保留期限筛选过期的逻辑删除文件编号，逐个调用本方法执行物理删除。
     *
     * @param fileId 文件编号
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    void physicalDeleteFile(String fileId) throws CommonException;
}
