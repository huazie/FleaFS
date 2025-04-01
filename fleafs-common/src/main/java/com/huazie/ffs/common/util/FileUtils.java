package com.huazie.ffs.common.util;

import com.huazie.ffs.common.exceptions.FleaFSException;
import com.huazie.fleaframework.common.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author huazie
 * @version 2.0.0
 * @since 2.0.0
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 从 File 对象获取文件扩展名
     *
     * @param file 文件对象
     * @return 扩展名（如 "txt"），无扩展名或错误时返回空字符串
     */
    public static String getFileExtension(File file) {
        if (file == null) return "";
        return getFileExtension(file.getName());
    }

    /**
     * 从完整文件名（可含路径）获取扩展名
     *
     * @param fullName 完整文件名或路径（如 "/path/to/file.txt"）
     * @return 扩展名（如 "txt"），无扩展名或错误时返回空字符串
     */
    public static String getFileExtension(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";

        // 提取纯文件名（去除路径）
        String fileName = new File(fullName).getName();

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // 无扩展名
        } else if (lastDotIndex == 0) {
            return ""; // 类似 ".gitignore" 的情况
        } else if (lastDotIndex == fileName.length() - 1) {
            return ""; // 类似 "file." 的情况
        }

        return fileName.substring(lastDotIndex + 1);
    }

    /**
     * 将输入流中的数据复制到指定文件中，自动创建目标文件的父目录（如果不存在）.
     *
     * @param inputStream 源输入流（非空）
     * @param file        目标文件（非空），若父目录不存在将尝试创建
     * @throws IOException 以下情况时抛出：
     *                     <ul>
     *                       <li>输入流或文件参数为空（实际抛出 {@link FleaFSException}）</li>
     *                       <li>无法创建目标文件的父目录</li>
     *                       <li>读取输入流或写入文件时发生I/O错误</li>
     *                     </ul>
     */
    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        ObjectUtils.checkEmpty(inputStream, FleaFSException.class, "inputStream must not be null");
        ObjectUtils.checkEmpty(file, FleaFSException.class, "file must not be null");

        File parent = file.getParentFile();
        if (parent != null && !parent.mkdirs() && !parent.exists()) {
            throw new IOException("Failed to create directory: " + parent);
        }

        try (InputStream in = inputStream;
             FileOutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new IOException("Copy failed", e);
        }
    }
}
