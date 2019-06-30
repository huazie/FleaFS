package com.huazie.ffs.module.upload.service.interfaces;

import com.huazie.ffs.pojo.upload.input.InputUploadAuthInfo;
import com.huazie.ffs.pojo.upload.output.OutputUploadAuthInfo;

/**
 * <p> Flea上传服务接口 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaUploadSV {

    OutputUploadAuthInfo uploadAuth(InputUploadAuthInfo input) throws Exception;
}
