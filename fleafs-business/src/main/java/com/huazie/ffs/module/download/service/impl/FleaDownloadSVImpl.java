package com.huazie.ffs.module.download.service.impl;

import com.huazie.ffs.module.download.service.interfaces.IFleaDownloadSV;
import com.huazie.ffs.pojo.download.input.InputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.input.InputFileDownloadInfo;
import com.huazie.ffs.pojo.download.output.OutputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.output.OutputFileDownloadInfo;
import com.huazie.fleaframework.common.DateFormatEnum;
import com.huazie.fleaframework.common.exception.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.RandomCode;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.common.exception.ServiceException;
import com.huazie.fleaframework.jersey.common.FleaJerseyManager;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Flea下载服务实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaDownloadSVImpl implements IFleaDownloadSV {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaDownloadSVImpl.class);

    @Override
    public OutputDownloadAuthInfo downloadAuth(InputDownloadAuthInfo input) throws CommonException {
        Object obj = null;
        if (LOGGER.isDebugEnabled()) {
            obj = new Object() {};
            LOGGER.debug1(obj, "Start");
        }

        String fileId = input.getFileId();
        if (StringUtils.isBlank(fileId)) {
            // 入参【{0}】不能为空
            throw new ServiceException("ERROR-SERVICE0000000001", "fileId");
        }

        OutputDownloadAuthInfo output = new OutputDownloadAuthInfo();
        output.setToken(RandomCode.toUUID());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(obj, "End");
        }

        return output;
    }

    @Override
    public OutputFileDownloadInfo fileDownload(InputFileDownloadInfo input) throws CommonException {

        Object obj = null;
        if (LOGGER.isDebugEnabled()) {
            obj = new Object() {};
            LOGGER.debug1(obj, "Start");
        }

        File file = new File("E:\\IMG.jpg");
        // 将文件添加到文件上下文中
        FleaJerseyManager.getManager().addFileDataBodyPart(file);

        OutputFileDownloadInfo output = new OutputFileDownloadInfo();
        output.setUploadAcctId("121212");
        output.setUploadSystemAcctId("1000");
        output.setUploadDate(DateUtils.date2String(null, DateFormatEnum.YYYYMMDDHHMMSS));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(obj, "End");
        }

        return output;
    }
}
