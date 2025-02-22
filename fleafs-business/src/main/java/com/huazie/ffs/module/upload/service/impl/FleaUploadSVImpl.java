package com.huazie.ffs.module.upload.service.impl;

import com.huazie.ffs.module.upload.service.interfaces.IFleaUploadSV;
import com.huazie.ffs.pojo.upload.input.InputFileUploadInfo;
import com.huazie.ffs.pojo.upload.input.InputUploadAuthInfo;
import com.huazie.ffs.pojo.upload.output.OutputFileUploadInfo;
import com.huazie.ffs.pojo.upload.output.OutputUploadAuthInfo;
import com.huazie.fleaframework.common.DateFormatEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.IOUtils;
import com.huazie.fleaframework.common.util.RandomCode;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.common.exceptions.ServiceException;
import com.huazie.fleaframework.jersey.common.FleaJerseyManager;
import com.huazie.fleaframework.jersey.common.data.FleaFileObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Flea上传服务实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaUploadSVImpl implements IFleaUploadSV {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaUploadSVImpl.class);

    @Override
    public OutputUploadAuthInfo uploadAuth(InputUploadAuthInfo input) throws CommonException {

        Object obj = null;
        if (LOGGER.isDebugEnabled()) {
            obj = new Object() {};
            LOGGER.debug1(obj, "Start");
        }

        OutputUploadAuthInfo output = new OutputUploadAuthInfo();
        output.setToken(RandomCode.toUUID());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(obj, "End");
        }

        return output;
    }

    @Override
    public OutputFileUploadInfo fileUpload(InputFileUploadInfo input) throws CommonException {

        Object obj = null;
        if (LOGGER.isDebugEnabled()) {
            obj = new Object() {};
            LOGGER.debug1(obj, "Start");
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
        try {
            IOUtils.toFile(new FileInputStream(uploadFile), "E:\\" + fileId + "_" +fileName);
        } catch (FileNotFoundException e) {

        }
        OutputFileUploadInfo outputFileUploadInfo = new OutputFileUploadInfo();
        outputFileUploadInfo.setFileId(fileId);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(obj, "End");
        }
        return outputFileUploadInfo;
    }
}
