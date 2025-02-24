package com.huazie.ffs.common.util;

import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * <p></p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FastDFSClient {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FastDFSClient.class);

    private static final String CONF_FILENAME = "fdfs_client.conf";
    private static StorageClient1 storageClient1 = null;

    static {
        try {
            LOGGER.info(" CONF_FILENAME:" + CONF_FILENAME);
            ClientGlobal.init(CONF_FILENAME);
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            if (trackerServer == null) {
                LOGGER.error("getTrackerServer return null");
            }
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null) {
                LOGGER.error("getStoreStorage return null");
            }
            storageClient1 = new StorageClient1(trackerServer, storageServer);
        } catch (Exception e) {
            LOGGER.error("连接异常", e);
        }
    }


    /**
     * 上传文件
     *
     * @param file     文件对象
     * @param fileName 文件名
     * @return 返回Null则为失败
     */
    public static String uploadFile(File file, String fileName) {
        return uploadFile(file, fileName, null);
    }

    /**
     * @param file     文件
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return 返回Null则为失败
     */
    public static String uploadFile(File file, String fileName, Map<String, String> metaList) {
        FileInputStream fis = null;
        try {
            NameValuePair[] meta_list = null;
            fis = new FileInputStream(file);

            int len = fis.available();
            byte[] file_buff = new byte[len];
            fis.read(file_buff);

            if (metaList != null) {
                meta_list = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String, String>> iterator = metaList.entrySet().iterator(); iterator.hasNext(); ) {
                    Map.Entry<String, String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    meta_list[index++] = new NameValuePair(name, value);
                }
            }
            return storageClient1.upload_file1(file_buff, FilenameUtils.getExtension(fileName), meta_list);
        } catch (Exception ex) {
            LOGGER.error("上传文件失败", ex);
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error("", e);
                }
            }
        }
    }

    /**
     * 根据组名和远程文件名来删除一个文件
     *
     * @param groupName 例如 "group1" 如果不指定该值，默认为group1
     * @param fileName  例如"M00/00/00/wKgxgk5HbLvfP86RAAAAChd9X1Y736.jpg"
     * @return 0为成功，非0为失败，具体为错误代码
     */
    public static int deleteFile(String groupName, String fileName) {
        try {
            return storageClient1.delete_file(groupName == null ? "group1" : groupName, fileName);
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return 0;
        }
    }

    /**
     * 根据fileId来删除一个文件（我们现在用的就是这样的方式，上传文件时直接将fileId保存在了数据库中）
     *
     * @param fileId file_id源码中的解释file_id the file id(including group name and filename);例如 group1/M00/00/00/ooYBAFM6MpmAHM91AAAEgdpiRC0012.xml
     * @return 0为成功，非0为失败，具体为错误代码
     */
    public static int deleteFile(String fileId) {
        try {
            return storageClient1.delete_file1(fileId);
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return 0;
        }
    }

    /**
     * 修改一个已经存在的文件
     *
     * @param oldFileId 原来旧文件的fileId, file_id源码中的解释file_id the file id(including group name and filename);例如 group1/M00/00/00/ooYBAFM6MpmAHM91AAAEgdpiRC0012.xml
     * @param file      新文件
     * @param filePath  新文件路径
     * @return 返回空则为失败
     */
    public static String modifyFile(String oldFileId, File file, String filePath) {
        String fileId;
        try {
            // 先上传
            fileId = uploadFile(file, filePath);
            if (fileId == null) {
                return null;
            }
            // 再删除
            int delResult = deleteFile(oldFileId);
            if (delResult != 0) {
                return null;
            }
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return null;
        }
        return fileId;
    }

    /**
     * 文件下载
     *
     * @param fileId 文件编号
     * @return 返回一个流
     */
    public static InputStream downloadFile(String fileId) {
        try {
            byte[] bytes = storageClient1.download_file1(fileId);
            return new ByteArrayInputStream(bytes);
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return null;
        }
    }
}
