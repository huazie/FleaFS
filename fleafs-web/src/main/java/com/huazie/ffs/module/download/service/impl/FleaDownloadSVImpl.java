package com.huazie.ffs.module.download.service.impl;

import com.huazie.ffs.module.download.service.interfaces.IFleaDownloadSV;
import com.huazie.ffs.pojo.download.input.InputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.input.InputFileDownloadInfo;
import com.huazie.ffs.pojo.download.output.OutputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.output.OutputFileDownloadInfo;
import com.huazie.frame.common.DateFormatEnum;
import com.huazie.frame.common.util.DateUtils;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.RandomCode;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.db.common.exception.ServiceException;
import com.huazie.frame.jersey.common.FleaJerseyManager;
import com.huazie.frame.jersey.common.data.FleaJerseyFileContext;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * <p> Flea下载服务实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaDownloadSVImpl implements IFleaDownloadSV {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleaDownloadSVImpl.class);

    @Override
    public OutputDownloadAuthInfo downloadAuth(InputDownloadAuthInfo input) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaDownloadSVImpl##downloadAuth(InputDownloadAuthInfo) Start");
        }

        String fileId = input.getFileId();
        if (StringUtils.isBlank(fileId)) {
            // 入参【{0}】不能为空
            throw new ServiceException("ERROR-SERVICE0000000001", "fileId");
        }

        OutputDownloadAuthInfo output = new OutputDownloadAuthInfo();
        output.setToken(RandomCode.toUUID());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaDownloadSVImpl##downloadAuth(InputDownloadAuthInfo) End");
        }

        return output;
    }

    @Override
    public OutputFileDownloadInfo fileDownload(InputFileDownloadInfo input) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaDownloadSVImpl##fileDownload(InputFileDownloadInfo) Start");
        }

        File file = new File("E:\\IMG.jpg");
        // 将文件添加到文件上下文中
        FleaJerseyManager.getManager().addFileDataBodyPart(file);

        OutputFileDownloadInfo output = new OutputFileDownloadInfo();
        output.setUploadAcctId("121212");
        output.setUploadSystemAcctId("1000");
        output.setUploadDate(DateUtils.date2String(null, DateFormatEnum.YYYYMMDDHHMMSS));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaDownloadSVImpl##fileDownload(InputFileDownloadInfo) End");
        }

        return output;
    }
}
