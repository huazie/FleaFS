package com.huazie.ffs.module.download.service.impl;

import com.huazie.ffs.module.download.service.interfaces.IFleaDownloadSV;
import com.huazie.ffs.pojo.download.input.InputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.input.InputFileDownloadInfo;
import com.huazie.ffs.pojo.download.output.OutputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.output.OutputFileDownloadInfo;
import com.huazie.frame.common.util.IOUtils;
import com.huazie.frame.common.util.RandomCode;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.db.common.exception.ServiceException;
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

        OutputFileDownloadInfo output = new OutputFileDownloadInfo();
        output.setFileName("绿色田园风光.jpg");
        output.setFileInput(IOUtils.toString(new File(FleaDownloadSVImpl.class.getResource("/file/绿色田园风光.jpg").getPath()), true));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaDownloadSVImpl##fileDownload(InputFileDownloadInfo) End");
        }

        return output;
    }
}
