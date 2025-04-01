package com.huazie.ffs.common.util;

import com.huazie.ffs.common.exceptions.FleaFSException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.ArrayUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FastDFS 客户端
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FastDFSClient {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FastDFSClient.class);
    private static String DEFAULT_CONFIG = "fdfs_client.conf";
    private static final Map<String, TrackerServer> TRACKER_POOL = new ConcurrentHashMap<>();
    private static final ThreadLocal<String> CONFIG_CONTEXT = new ThreadLocal<>();

    // 私有构造防止实例化
    private FastDFSClient() {
    }

    /**
     * 设置全局默认配置
     *
     * @param configPath FastDFS客户端配置路径
     */
    public static synchronized void setDefaultConfig(String configPath) {
        TRACKER_POOL.clear(); // 清除旧配置缓存
        CONFIG_CONTEXT.remove();
        DEFAULT_CONFIG = configPath;
    }

    /**
     * 获取实际使用的配置路径
     *
     * @return FastDFS客户端配置路径
     */
    private static String getEffectiveConfig() {
        String contextConfig = CONFIG_CONTEXT.get();
        return StringUtils.isNotBlank(contextConfig) ? contextConfig : DEFAULT_CONFIG;
    }

    /**
     * 获取 TrackerServer（线程安全初始化）
     *
     * @param configPath FastDFS客户端配置路径
     * @return 可用的TrackerServer实例（与配置文件绑定的长连接）
     * @throws FleaFSException 配置文件无效、Tracker节点不可达或并发初始化异常时抛出，
     */
    private static TrackerServer getTrackerServer(String configPath) {
        TrackerServer trackerServer = TRACKER_POOL.get(configPath);
        if (trackerServer == null) {
            synchronized (TRACKER_POOL) {
                trackerServer = TRACKER_POOL.get(configPath);
                if (trackerServer == null) {
                    try {
                        ClientGlobal.init(configPath);
                        TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
                        trackerServer = trackerClient.getTrackerServer();
                        if (trackerServer == null) {
                            throw new FleaFSException("TrackerServer初始化失败");
                        }
                        TRACKER_POOL.put(configPath, trackerServer);
                    } catch (Exception e) {
                        LOGGER.error("FastDFS配置初始化失败: {}", configPath, e);
                        throw new FleaFSException("FastDFS配置初始化失败", e);
                    }
                }
            }
        }
        return trackerServer;
    }

    /**
     * 获取FastDFS存储客户端（含Tracker路由与负载均衡）
     *
     * @return 线程安全的{@link StorageClient1}实例（需配合try-with-resources使用）
     * @throws FleaFSException 配置文件无效、TrackerServer不可达或存储节点选择失败时抛出
     * @see TrackerClient#getStoreStorage(TrackerServer) 存储节点选择逻辑
     */
    private static StorageClient1 getStorageClient() {
        String configPath = getEffectiveConfig();
        try {
            TrackerServer trackerServer = getTrackerServer(configPath);
            StorageServer storageServer = new TrackerClient().getStoreStorage(trackerServer);
            return new StorageClient1(trackerServer, storageServer);
        } catch (Exception e) {
            throw new FleaFSException("获取Storage连接失败", e);
        }
    }

    /**
     * 上传文件
     *
     * @param file     待上传的文件对象（本地文件）
     * @param fileName 自定义文件名（建议包含扩展名，如"fleafs.png"）
     * @return 文件在FastDFS中的完整路径（格式：groupName/relativePath/filename，如"group1/M00/00/00/wKhkgmOq7Z6ACQAAAAADQ7qXIk12.png"）
     */
    public static String uploadFile(File file, String fileName) {
        return uploadFile(file, fileName, null);
    }

    /**
     * 上传文件（带元数据）
     *
     * @param file     待上传的本地文件（非空，长度需 > 0）
     * @param fileName 完整文件名（需包含扩展名，如"fleafs.png"）
     * @param meta     自定义元数据（键值对，键不能含'_'，值≤255字符，可为null）
     * @return 文件在FastDFS中的完整路径（格式：groupName/relativePath/filename，如"group1/M00/00/00/wKhkgmOq7Z6ACQAAAAADQ7qXIk12.png"）
     */
    public static String uploadFile(File file, String fileName, Map<String, String> meta) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = readAllBytes(fis);
            return doUpload(bytes, fileName, meta);
        } catch (Exception e) {
            throw new FleaFSException("文件上传失败", e);
        } finally {
            close(fis);
        }
    }

    /**
     * 上传文件
     *
     * @param inputStream 文件输入流
     * @param fileName    完整文件名（需包含扩展名，如"fleafs.png"）
     * @return 文件在FastDFS中的完整路径（格式：groupName/relativePath/filename，如"group1/M00/00/00/wKhkgmOq7Z6ACQAAAAADQ7qXIk12.png"）
     */
    public static String uploadFile(InputStream inputStream, String fileName) {
        return uploadFile(inputStream, fileName, null);
    }

    /**
     * 上传文件（带元数据）
     *
     * @param inputStream 文件输入流
     * @param fileName    完整的文件名（需包含扩展名，如"fleafs.png"）
     * @param meta        自定义元数据（键值对，键不能含'_'，值≤255字符，可为null）
     * @return 文件在FastDFS中的完整路径（格式：groupName/relativePath/filename，如"group1/M00/00/00/wKhkgmOq7Z6ACQAAAAADQ7qXIk12.png"）
     */
    public static String uploadFile(InputStream inputStream, String fileName, Map<String, String> meta) {
        try {
            byte[] bytes = readAllBytes(inputStream);
            return doUpload(bytes, fileName, meta);
        } catch (Exception e) {
            throw new FleaFSException("文件上传失败", e);
        } finally {
            close(inputStream);
        }
    }

    /**
     * 上传文件（内部）
     *
     * @param byteArr  字节数组
     * @param fileName 完整的文件名
     * @param meta     自定义元数据（键值对，键不能含'_'，值≤255字符，可为null）
     * @return 文件在FastDFS中的完整路径（格式：groupName/relativePath/filename，如"group1/M00/00/00/wKhkgmOq7Z6ACQAAAAADQ7qXIk12.png"）
     */
    private static String doUpload(byte[] byteArr, String fileName, Map<String, String> meta) {
        NameValuePair[] metaPairs = convertMeta(meta);
        String extension = FileUtils.getFileExtension(fileName);
        try {
            return getStorageClient().upload_file1(byteArr, extension, metaPairs);
        } catch (Exception e) {
            throw new FleaFSException("文件上传到FastDFS失败", e);
        }
    }

    /**
     * 从输入流中读取所有字节并返回对应的字节数组
     *
     * @param inputStream 待读取的输入流
     * @return 一个包含输入流中所有字节的字节数组。如果输入流为空，则返回一个空数组。
     * @throws IOException 如果在读取输入流时发生I/O错误，则抛出此异常。
     */
    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }

    /**
     * 下载文件（小文件优化，返回字节数组流）
     *
     * @param fileId FastDFS文件ID（格式：groupName/relativePath/filename，如"group1/M00/00/00/wKhkgmOq7Z6ACQAAAAADQ7qXIk12.png"
     * @return 文件字节数组流（需调用者手动关闭，否则可能内存泄漏）
     */
    public static InputStream downloadFile(String fileId) {
        try {
            byte[] bytes = getStorageClient().download_file1(fileId);
            if (ArrayUtils.isEmpty(bytes)) {
                throw new FleaFSException("当前文件【fileId=" + fileId + "】不存在!");
            }
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            throw new FleaFSException("文件下载失败", e);
        }
    }

    /**
     * 删除FastDFS文件（含错误码解析）
     *
     * @param fileId 文件完整ID（格式：groupName/relativePath/filename，如"group1/M00/00/00/test.jpg"）
     * @throws FleaFSException 文件不存在（错误码2）、Storage服务不可达（错误码110）或权限不足时抛出
     */
    public static void deleteFile(String fileId) {
        try {
            int result = getStorageClient().delete_file1(fileId);
            if (result != 0) {
                throw new FleaFSException("文件删除失败, 错误码: " + result);
            }
        } catch (Exception e) {
            throw new FleaFSException("文件删除异常", e);
        }
    }

    /**
     * 关闭资源
     *
     * @param closeable 待关闭的资源
     */
    private static void close(Closeable closeable) {
        if (ObjectUtils.isNotEmpty(closeable)) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error("资源关闭异常", e);
            }
        }
    }

    /**
     * 元数据转换
     *
     * @param meta 待转换的元数据Map
     * @return 键值对数组
     */
    private static NameValuePair[] convertMeta(Map<String, String> meta) {
        if (meta == null || meta.isEmpty()) return null;
        NameValuePair[] pairs = new NameValuePair[meta.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : meta.entrySet()) {
            pairs[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }
        return pairs;
    }
}