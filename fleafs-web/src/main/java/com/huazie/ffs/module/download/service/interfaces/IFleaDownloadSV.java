package com.huazie.ffs.module.download.service.interfaces;

import com.huazie.ffs.pojo.download.input.InputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.input.InputFileDownloadInfo;
import com.huazie.ffs.pojo.download.output.OutputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.output.OutputFileDownloadInfo;

/**
 * <p> Flea下载服务接口 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaDownloadSV {

    /**
     * <p> 下载授权 </p>
     *
     * @param input 下载授权业务入参
     * @return 下载授权业务出参
     * @throws Exception
     * @since 1.0.0
     */
    OutputDownloadAuthInfo downloadAuth(InputDownloadAuthInfo input) throws Exception;

    /**
     * <p> 文件下载 </p>
     *
     * @param input 文件下载业务入参
     * @return 文件下载业务出参
     * @throws Exception
     * @since 1.0.0
     */
    OutputFileDownloadInfo fileDownload(InputFileDownloadInfo input) throws Exception;

}
