package com.huazie.ffs;

import com.huazie.ffs.common.util.FastDFSClient;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * <p></p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FastDFSClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastDFSClientTest.class);

    @Test
    public void uploadFile() {

        File file = new File("F:\\FileRecv\\project\\FleaFS\\fleafs-web\\src\\main\\resources\\file\\绿色田园风光.jpg");
        String fileName = "绿色田园风光.jpg";
        String fileId = FastDFSClient.uploadFile(file, fileName);
        LOGGER.debug("FILE_ID : {}", fileId);
    }

    @Test
    public void downloadFile() throws Exception {
        InputStream inputStream = FastDFSClient.downloadFile("group1/M00/00/00/wKhdgF1k4l-ATxyUAARILn22Mjc649.jpg");
        File file = new File("E:\\绿色.jpg");
        FileUtils.copyInputStreamToFile(inputStream, file);
    }

    @Test
    public void deleteFile() {
        int result = FastDFSClient.deleteFile("group1/M00/00/00/wKhdgF1kmzWAa49PAABI_vi2v8M190.jpg");
        LOGGER.debug("RESULT : {}", result);

    }

}
