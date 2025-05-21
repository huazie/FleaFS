package com.huazie.ffs.base.util;

import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.common.FileSizeUnitEnum;
import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.ffs.common.util.FileUtils;
import com.huazie.fleaframework.common.util.NumberUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.common.exceptions.ServiceException;

import java.io.File;

/**
 * FleaFS校验工具类
 *
 * @author huazie
 * @version 2.0.0
 * @since 2.0.0
 */
public class FleaFSCheck {

    private FleaFSCheck() {
    }

    /**
     * 校验 obj 是否为空，如果为空，则抛出相应的异常。
     *
     * @param obj     待判断对象实例
     * @param objName 对象名称
     * @since 2.0.0
     */
    public static void checkEmpty(Object obj, String objName) {
        // ERROR-SERVICE0000000000 【{0}】不能为空！
        ObjectUtils.checkEmpty(obj, ServiceException.class, "ERROR-SERVICE0000000000", objName);
    }

    /**
     * 校验 obj 是否为空，如果为空，则抛出相应的异常。
     *
     * @param obj     待判断对象实例
     * @param objName 对象名称
     * @since 2.0.0
     */
    public static void checkEmpty1(Object obj, String objName) {
        // ERROR-SERVICE0000000001 入参【{0}】不能为空！
        ObjectUtils.checkEmpty(obj, ServiceException.class, "ERROR-SERVICE0000000001", objName);
    }

    /**
     * 检验 str 是否为空，如果为空，则抛出相应的异常。
     *
     * @param str     待判断字符串对象实例
     * @param strName 字符串对象名称
     * @since 2.0.0
     */
    public static void checkBlank(String str, String strName) {
        // ERROR-SERVICE0000000000 【{0}】不能为空
        StringUtils.checkBlank(str, ServiceException.class, "ERROR-SERVICE0000000000", strName);
    }

    /**
     * 检验 str 是否为空，如果为空，则抛出相应的异常。
     *
     * @param str     待判断字符串对象实例
     * @param strName 字符串对象名称
     * @since 2.0.0
     */
    public static void checkBlank1(String str, String strName) {
        // ERROR-SERVICE0000000001 入参【{0}】不能为空！
        StringUtils.checkBlank(str, ServiceException.class, "ERROR-SERVICE0000000001", strName);
    }

    /**
     * 校验 entity 是否存在，如果不存在，则抛出相应的异常。
     *
     * @param entity       Flea文件类目
     * @param categoryId   类目编号
     * @param categoryCode 类目编码
     * @since 1.0.0
     */
    public static void checkFleaFileCategory(FleaFileCategory entity, Long categoryId, String categoryCode) {
        // ERROR-SERVICE0000000003 文件类目【{0}】不存在，请检查！
        String param = "";
        if (NumberUtils.isPositiveNumber(categoryId)) {
            param = StringUtils.valueOf(categoryId);
        } else if (StringUtils.isNotBlank(categoryCode)) {
            param = categoryCode;
        }
        ObjectUtils.checkEmpty(entity, ServiceException.class, "ERROR-SERVICE0000000003", param);
    }

    /**
     * 校验 entity 是否存在，如果不存在，则抛出相应的异常。
     *
     * @param entity Flea文件鉴权信息
     * @param token  鉴权令牌
     * @since 1.0.0
     */
    public static void checkFleaTokenInfo(FleaTokenInfo entity, String token) {
        // ERROR-SERVICE0000000004 鉴权令牌【{0}】非法或已失效，请检查！
        ObjectUtils.checkEmpty(entity, ServiceException.class, "ERROR-SERVICE0000000004", token);
    }

    /**
     * 校验 entity 是否存在，如果不存在，则抛出相应的异常。
     *
     * @param entity Flea文件信息
     * @param fileId 文件编号
     * @since 1.0.0
     */
    public static void checkFleaFileInfo(FleaFileInfo entity, String fileId) {
        // ERROR-SERVICE0000000005 文件【{0}】不存在或已删除，请检查！
        ObjectUtils.checkEmpty(entity, ServiceException.class, "ERROR-SERVICE0000000005", fileId);
    }

    /**
     * 校验实际的文件的大小是否超过限制的文佳最大值【单位：MB】
     *
     * @param file        实际的文件对象
     * @param maxFileSize 文件最大值【单位：MB】
     * @since 1.0.0
     */
    public static void checkFileSize(File file, double maxFileSize) {
        if (!NumberUtils.isPositiveNumber(maxFileSize))
            // 默认设置为100M
            maxFileSize = (double) FleaFSConstants.FileCategoryConstants.MAX_FILE_SIZE;
        // 获取文件的实际大小【单位：B】
        double fileSize = FileUtils.getFileSize(file, FileSizeUnitEnum.BYTES);
        // 将文件最大值转换为字节数
        double maxFileSizeBytes = FileSizeUnitEnum.MEGABYTES.convertToBytes(maxFileSize);
        // ERROR-SERVICE0000000006 当前文件大小【{0}】超过限制的文件最大值【{1}】
        int result = Double.compare(maxFileSizeBytes, fileSize);
        NumberUtils.checkNonPositiveNumber(result, ServiceException.class, "ERROR-SERVICE0000000006",
                FileUtils.getSmartFileSize(file), FileUtils.getSmartFileSize(maxFileSizeBytes));
    }
}
