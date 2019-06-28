package com.huazie.ffs.module.upload.service.interfaces;

import com.huazie.ffs.module.upload.pojo.InputUploadAuthInfo;
import com.huazie.ffs.module.upload.pojo.OutputUploadAuthInfo;

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
