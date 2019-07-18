package com.huazie.ffs.module.download.service.impl;

import com.huazie.ffs.module.download.service.interfaces.IFleaDownloadSV;
import com.huazie.ffs.pojo.download.input.InputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.input.InputFileDownloadInfo;
import com.huazie.ffs.pojo.download.output.OutputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.output.OutputFileDownloadInfo;
import com.huazie.frame.common.util.RandomCode;
import com.huazie.frame.common.util.ResourcesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p> Flea下载服务实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaDownloadSVImpl implements IFleaDownloadSV {

    private final static Logger LOGGER = LoggerFactory.getLogger(FleaDownloadSVImpl.class);

    @Override
    public OutputDownloadAuthInfo downloadAuth(InputDownloadAuthInfo input) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaDownloadSVImpl##downloadAuth(InputDownloadAuthInfo) Start");
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
        output.setFileName("test.txt");
        output.setFileInputStream(ResourcesUtil.getInputStreamFromClassPath("file\\test.txt"));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaDownloadSVImpl##fileDownload(InputFileDownloadInfo) End");
        }

        return output;
    }
}
