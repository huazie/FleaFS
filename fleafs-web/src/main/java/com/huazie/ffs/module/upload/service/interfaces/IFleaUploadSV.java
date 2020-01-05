package com.huazie.ffs.module.upload.service.interfaces;

import com.huazie.ffs.pojo.upload.input.InputFileUploadInfo;
import com.huazie.ffs.pojo.upload.input.InputUploadAuthInfo;
import com.huazie.ffs.pojo.upload.output.OutputFileUploadInfo;
import com.huazie.ffs.pojo.upload.output.OutputUploadAuthInfo;

/**
 * <p> Flea上传服务接口 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaUploadSV {

    /**
     * <p> 上传授权 </p>
     *
     * @param input 上传授权业务入参
     * @return 上传授权业务出参
     * @throws Exception
     * @since 1.0.0
     */
    OutputUploadAuthInfo uploadAuth(InputUploadAuthInfo input) throws Exception;

    /**
     * <p> 文件上传 </p>
     *
     * @param input 文件上传入参（包含上传鉴权令牌）
     * @return 文件上传出参（包含文件编号）
     * @throws Exception
     * @since 1.0.0
     */
    OutputFileUploadInfo fileUpload(InputFileUploadInfo input) throws Exception;
}
