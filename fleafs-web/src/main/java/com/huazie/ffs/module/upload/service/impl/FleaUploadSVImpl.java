package com.huazie.ffs.module.upload.service.impl;

import com.huazie.ffs.pojo.upload.input.InputUploadAuthInfo;
import com.huazie.ffs.pojo.upload.output.OutputUploadAuthInfo;
import com.huazie.ffs.module.upload.service.interfaces.IFleaUploadSV;
import com.huazie.frame.common.util.RandomCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p> Flea上传服务实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaUploadSVImpl implements IFleaUploadSV {

    private final static Logger LOGGER = LoggerFactory.getLogger(FleaUploadSVImpl.class);

    @Override
    public OutputUploadAuthInfo uploadAuth(InputUploadAuthInfo input) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaUploadSVImpl##uploadAuth(InputUploadAuthInfo) Start");
        }

        OutputUploadAuthInfo output = new OutputUploadAuthInfo();
        output.setToken(RandomCode.toUUID());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaUploadSVImpl##uploadAuth(InputUploadAuthInfo) End");
        }

        return output;
    }

}
