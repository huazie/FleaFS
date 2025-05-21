package com.huazie.ffs.common.util;

import com.huazie.ffs.common.FileSizeUnitEnum;
import com.huazie.ffs.common.exceptions.FleaFSException;
import com.huazie.fleaframework.common.exceptions.FleaException;
import com.huazie.fleaframework.common.util.ExceptionUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
     * @throws FleaException 以下情况时抛出：
     *                     <ul>
     *                     <li>输入流或文件参数为空（实际抛出 {@link FleaFSException}）</li>
     *                     <li>无法创建目标文件的父目录</li>
     *                     <li>读取输入流或写入文件时发生I/O错误</li>
     *                     </ul>
     */
    public static void copyInputStreamToFile(InputStream inputStream, File file) throws FleaException {
        ObjectUtils.checkEmpty(inputStream, FleaFSException.class, "inputStream must not be null");
        ObjectUtils.checkEmpty(file, FleaFSException.class, "file must not be null");

        File parent = file.getParentFile();
        if (parent != null && !parent.mkdirs() && !parent.exists()) {
            throw new FleaException("Failed to create directory: " + parent);
        }

        try (InputStream in = inputStream;
             FileOutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            ExceptionUtils.throwFleaException(FleaFSException.class, "Copy failed", e);
        }
    }

    /**
     * 获取文件大小（指定单位）
     *
     * @param file 文件对象
     * @param unit 目标单位
     * @return 文件大小（带两位小数）
     * @throws FleaException 当文件不存在或读取失败时抛出
     * @since 1.0.0
     */
    public static double getFileSize(File file, FileSizeUnitEnum unit) throws FleaException {
        return getFileSize(file.toPath(), unit);
    }

    /**
     * 自动选择合适的单位获取文件大小
     *
     * @param file 文件对象
     * @return 带单位的文件大小字符串（自动保留两位小数）
     * @throws FleaException 当文件不存在或读取失败时抛出
     * @since 1.0.0
     */
    public static String getSmartFileSize(File file) throws FleaException {
        return getSmartFileSize(file.toPath());
    }

    /**
     * 获取文件大小（指定单位）
     *
     * @param filePath 文件路径
     * @param unit     目标单位
     * @return 文件大小（带两位小数）
     * @throws FleaException 当文件不存在或读取失败时抛出
     * @since 1.0.0
     */
    public static double getFileSize(String filePath, FileSizeUnitEnum unit) throws FleaException {
        return getFileSize(Paths.get(filePath), unit);
    }

    /**
     * 自动选择合适的单位获取文件大小
     *
     * @param filePath 文件路径
     * @return 带单位的文件大小字符串（自动保留两位小数）
     * @throws FleaException 当文件不存在或读取失败时抛出
     * @since 1.0.0
     */
    public static String getSmartFileSize(String filePath) throws FleaException {
        return getSmartFileSize(Paths.get(filePath));
    }

    /**
     * 自动将字节数转换为易读的文件大小字符串（智能选择单位）。
     *
     * 根据字节大小自动选择最合适的单位（如 B/KB/MB/GB/TB），并格式化为保留两位小数的字符串。
     * 例如：
     *   - 1024 字节 → "1.00 KB"
     *   - 1.5*1024² 字节 → "1.50 MB"
     *
     * @param fileSize 文件大小（单位：字节），必须为非负数
     * @return 格式化后的字符串，格式为 "数值 单位"（如 "2.50 GB"）
     * @throws FleaException 如果输入为负数或计算过程中发生错误
     */
    public static String getSmartFileSize(double fileSize) throws FleaException {
        return formatBytesSmart(fileSize);
    }

    private static double getFileSize(Path path, FileSizeUnitEnum unit) throws FleaException {
        validateFile(path);
        return unit.convertFromBytes(getSize(path));
    }

    private static String getSmartFileSize(Path path) throws FleaException {
        validateFile(path);
        return formatBytesSmart(getSize(path));
    }

    private static void validateFile(Path path) throws FleaException {
        if (!Files.exists(path)) {
            ExceptionUtils.throwFleaException(FleaFSException.class, "File not found: " + path);
        }
        if (!Files.isRegularFile(path)) {
            ExceptionUtils.throwFleaException(FleaFSException.class, "Path is not a regular file: " + path);
        }
    }

    private static long getSize(Path path) throws FleaException {
        try {
            return Files.size(path);
        } catch (IOException e) {
            ExceptionUtils.throwFleaException(FleaFSException.class, e);
        }
        return 0;
    }

    private static String formatBytesSmart(double bytes) {
        FileSizeUnitEnum[] units = FileSizeUnitEnum.values();
        for (int i = units.length - 1; i >= 0; i--) {
            if (bytes >= units[i].getBytes()) {
                double value = bytes / units[i].getBytes();
                return String.format("%.2f %s", value, units[i].getUnit());
            }
        }
        return bytes + " B";
    }
}
