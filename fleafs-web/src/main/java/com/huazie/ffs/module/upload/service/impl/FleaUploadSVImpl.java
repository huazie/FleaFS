package com.huazie.ffs.module.upload.service.impl;

import com.huazie.ffs.module.upload.service.interfaces.IFleaUploadSV;
import com.huazie.ffs.pojo.upload.input.InputFileUploadInfo;
import com.huazie.ffs.pojo.upload.input.InputUploadAuthInfo;
import com.huazie.ffs.pojo.upload.output.OutputFileUploadInfo;
import com.huazie.ffs.pojo.upload.output.OutputUploadAuthInfo;
import com.huazie.frame.common.DateFormatEnum;
import com.huazie.frame.common.util.DateUtils;
import com.huazie.frame.common.util.IOUtils;
import com.huazie.frame.common.util.RandomCode;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.db.common.exception.ServiceException;
import com.huazie.frame.jersey.common.FleaJerseyManager;
import com.huazie.frame.jersey.common.data.FleaFileObject;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

/**
 * <p> Flea上传服务实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaUploadSVImpl implements IFleaUploadSV {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleaUploadSVImpl.class);

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

    @Override
    public OutputFileUploadInfo fileUpload(InputFileUploadInfo input) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaUploadSVImpl##fileUpload(InputFileUploadInfo) Start");
        }

        String token = input.getToken();
        if (StringUtils.isBlank(token)) {
            // 入参【{0}】不能为空
            throw new ServiceException("ERROR-SERVICE0000000001", "上传鉴权令牌【token】");
        }

        FleaFileObject fileObject = FleaJerseyManager.getManager().getFileObject();
        String fileName = fileObject.getFileName();
        File uploadFile = fileObject.getFile();

        String fileId = DateUtils.date2String(null, DateFormatEnum.YYYYMMDD) + RandomCode.toUUID();
        IOUtils.toFile(new FileInputStream(uploadFile), "E:\\" + fileId + "_" +fileName);
        OutputFileUploadInfo outputFileUploadInfo = new OutputFileUploadInfo();
        outputFileUploadInfo.setFileId(fileId);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaUploadSVImpl##fileUpload(InputFileUploadInfo) Start");
        }
        return outputFileUploadInfo;
    }
}
