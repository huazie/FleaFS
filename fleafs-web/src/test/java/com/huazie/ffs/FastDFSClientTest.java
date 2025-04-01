package com.huazie.ffs;

import com.huazie.ffs.common.util.FastDFSClient;
import com.huazie.ffs.common.util.FileUtils;
import com.huazie.fleaframework.common.util.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * FastDFS客户端测试
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FastDFSClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastDFSClientTest.class);

    @Test
    public void uploadFile() {
        InputStream inputStream = IOUtils.getInputStreamFromClassPath("file/绿色田园风光.jpg");
        String fileName = "绿色田园风光.jpg";
        String fileId = FastDFSClient.uploadFile(inputStream, fileName);
        LOGGER.debug("FILE_ID : {}", fileId);
    }

    @Test
    public void downloadFile() throws Exception {
        InputStream inputStream = FastDFSClient.downloadFile("group1/M00/00/00/rBbMOWfqU3CAH_cdAARILk7ifpI048.jpg");
        File file = new File("E:\\绿色.jpg");
        FileUtils.copyInputStreamToFile(inputStream, file);
    }

    @Test
    public void deleteFile() {
        FastDFSClient.deleteFile("group1/M00/00/00/rBbMOWfqU3CAH_cdAARILk7ifpI048.jpg");
    }

}
