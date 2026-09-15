package com.huazie.ffs.module.download.service.impl;

import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.base.service.interfaces.IFleaFileAttrSV;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
import com.huazie.ffs.base.service.interfaces.IFleaFileInfoSV;
import com.huazie.ffs.base.service.interfaces.IFleaTokenInfoSV;
import com.huazie.ffs.base.util.FleaFSCheck;
import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.ffs.common.util.EncryptionUtils;
import com.huazie.ffs.common.util.FastDFSClient;
import com.huazie.ffs.common.util.FileUtils;
import com.huazie.ffs.module.download.service.interfaces.IFleaDownloadSV;
import com.huazie.ffs.pojo.download.input.InputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.input.InputFileDownloadInfo;
import com.huazie.ffs.pojo.download.output.OutputDownloadAuthInfo;
import com.huazie.ffs.pojo.download.output.OutputFileDownloadInfo;
import com.huazie.ffs.util.FleaFSUtils;
import com.huazie.fleaframework.common.DateFormatEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.RandomCode;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.jpa.transaction.FleaTransactional;
import com.huazie.fleaframework.jersey.common.FleaJerseyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Flea下载服务实现类，主要功能如下：
 * <p> 下载鉴权，用于获取文件下载所需的鉴权token（token会落库，并关联文件信息）
 * <p> 文件下载，用于实际文件的下载，内部通过FastDFS API操作文件下载
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaDownloadSVImpl implements IFleaDownloadSV {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaDownloadSVImpl.class);

    /**
     * 下载临时文件根目录名（位于系统临时目录下）
     */
    private static final String TEMP_DIR_NAME = "fleafs-download";

    /**
     * 下载临时文件保留时长【单位：毫秒】，超过该时长的历史临时文件将被清理
     */
    private static final long TEMP_FILE_KEEP_MILLIS = 60 * 60 * 1000L;

    private IFleaFileInfoSV fleaFileInfoSV;

    private IFleaTokenInfoSV fleaTokenInfoSV;

    private IFleaFileAttrSV fleaFileAttrSV;

    private IFleaFileCategorySV fleaFileCategorySV;

    @Autowired
    @Qualifier("fleaFileInfoSV")
    public void setFleaFileInfoSV(IFleaFileInfoSV fleaFileInfoSV) {
        this.fleaFileInfoSV = fleaFileInfoSV;
    }

    @Autowired
    @Qualifier("fleaTokenInfoSV")
    public void setFleaTokenInfoSV(IFleaTokenInfoSV fleaTokenInfoSV) {
        this.fleaTokenInfoSV = fleaTokenInfoSV;
    }

    @Autowired
    @Qualifier("fleaFileAttrSV")
    public void setFleaFileAttrSV(IFleaFileAttrSV fleaFileAttrSV) {
        this.fleaFileAttrSV = fleaFileAttrSV;
    }

    @Autowired
    @Qualifier("fleaFileCategorySV")
    public void setFleaFileCategorySV(IFleaFileCategorySV fleaFileCategorySV) {
        this.fleaFileCategorySV = fleaFileCategorySV;
    }

    @Override
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs", seq = "'SEQ=' + #input.fileId")
    public OutputDownloadAuthInfo downloadAuth(InputDownloadAuthInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "下载鉴权业务入参");

        String fileId = input.getFileId();
        FleaFSCheck.checkBlank1(fileId, "文件编号(fileId)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "下载鉴权，fileId = {}", fileId);

        // 校验文件信息是否存在且可下载
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileDownloadable(fileInfo, fileId);

        // 查询文件关联的类目编号【文件属性表中获取】
        Long categoryId = fleaFileAttrSV.queryFileCategoryId(fileId);

        // 获取文件类目，并校验类目是否启用文件下载操作
        FleaFileCategory fleaFileCategory = fleaFileCategorySV.query(categoryId);
        FleaFSCheck.checkFleaFileCategory(fleaFileCategory, categoryId, null);
        FleaFSCheck.checkOperationState(fleaFileCategory, OperateTypeEnum.DOWNLOAD, categoryId);

        // 生成下载鉴权token，为保证token和文件信息落在同一分库，token末位同文件编号末位
        String token = generateToken(fileId);

        // 生成token鉴权信息
        Date expiryDate = DateUtils.getTime(Calendar.MINUTE, FleaFSUtils.getTokenExpMinutes());
        Map<String, Object> extendMap = new HashMap<>();
        extendMap.put(FleaFSEntityConstants.E_EXPIRY_DATE, expiryDate);
        fleaTokenInfoSV.saveFleaTokenInfo(token, fileId, categoryId, OperateTypeEnum.DOWNLOAD, extendMap);

        LOGGER.debug1(obj, "下载鉴权，token = {}", token);

        OutputDownloadAuthInfo output = new OutputDownloadAuthInfo();
        output.setToken(token);

        return output;
    }

    @Override
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs", seq = "'SEQ=' + #input.token")
    public OutputFileDownloadInfo fileDownload(InputFileDownloadInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "文件下载业务入参");

        String token = input.getToken();
        FleaFSCheck.checkBlank1(token, "下载鉴权令牌(token)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "文件下载，token = {}", token);

        // 根据token查询有效的Flea鉴权信息
        FleaTokenInfo fleaTokenInfo = fleaTokenInfoSV.queryValidFleaTokenInfo(token);
        FleaFSCheck.checkFleaTokenInfo(fleaTokenInfo, token);

        // 获取文件信息
        String fileId = fleaTokenInfo.getFileId();
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileDownloadable(fileInfo, fileId);

        // 通过FastDFS下载文件
        InputStream inputStream = FastDFSClient.downloadFile(fileInfo.getFastdfsId());

        // 文件加密场景，需在写入临时文件前流式解密
        InputStream fileStream = decryptFileStream(fileInfo, inputStream);

        // 将下载的文件写入临时文件（文件名保持真实文件名，保证下载响应中的文件名正确）
        File tempFile = writeTempFile(fileInfo, fileStream);

        LOGGER.debug1(obj, "文件下载，fileName = {}", fileInfo.getFileName());

        // 将文件添加到文件上下文中【框架在响应输出阶段才读取该文件，此处不可提前清理】
        FleaJerseyManager.getManager().addFileDataBodyPart(tempFile);

        // 失效Flea鉴权信息（下载token一次性使用）
        expireFleaTokenInfo(fleaTokenInfo);

        OutputFileDownloadInfo output = new OutputFileDownloadInfo();
        output.setUploadAcctId(StringUtils.valueOf(fileInfo.getUserId()));
        output.setUploadSystemAcctId(StringUtils.valueOf(fileInfo.getSystemUserId()));
        output.setUploadDate(DateUtils.date2String(fileInfo.getCreateDate(), DateFormatEnum.YYYYMMDDHHMMSS));

        return output;
    }

    /**
     * 按文件类目配置的加密方式解密文件输入流
     * <p> 文件信息中的密钥（secret_key）非空，说明该文件上传（或更新）时已加密，
     * 此处按文件所属类目配置的加密方式流式解密；密钥为空则说明文件未加密，原样返回。
     * <p> 解密流式处理，不做整文件缓存，调用方负责关闭流。
     *
     * @param fileInfo    文件信息
     * @param inputStream 从FastDFS下载的加密文件输入流
     * @return 解密后的文件输入流【文件未加密时原样返回】
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private InputStream decryptFileStream(FleaFileInfo fileInfo, InputStream inputStream) throws CommonException {
        String secretKey = fileInfo.getSecretKey();
        if (ObjectUtils.isEmpty(secretKey)) {
            // 文件未加密，无需解密
            return inputStream;
        }

        Object obj = new Object() {};
        LOGGER.debug1(obj, "文件下载，文件已加密，fileId = {}, fileName = {}", fileInfo.getFileId(), fileInfo.getFileName());

        // 查询文件关联的类目编号【文件属性表中获取】，并校验类目配置的加密方式是否支持
        Long categoryId = fleaFileAttrSV.queryFileCategoryId(fileInfo.getFileId());
        FleaFileCategory fleaFileCategory = fleaFileCategorySV.query(categoryId);
        FleaFSCheck.checkFleaFileCategory(fleaFileCategory, categoryId, null);

        String encryptType = fleaFileCategory.getEncryptType();
        FleaFSCheck.checkEncryptionType(encryptType, categoryId);

        return EncryptionUtils.decryptFile(inputStream, encryptType, secretKey);
    }

    /**
     * 生成下载鉴权token，并保证token末位与文件编号末位相同
     * <p> 文件编号末位同token末位，可保证下载鉴权信息和文件信息落在同一个分库
     *
     * @param fileId 文件编号
     * @return 下载鉴权token
     */
    private String generateToken(String fileId) {
        String token = fleaTokenInfoSV.generateToken();
        String last = StringUtils.subStrLast(fileId, 1);
        return StringUtils.strCat(token.substring(0, token.length() - 1), last);
    }

    /**
     * 将下载的文件写入临时文件，临时文件名保持真实文件名
     * <p> 临时文件存放于系统临时目录下的下载临时子目录中；
     * <p> 注意：框架在响应输出阶段才读取该临时文件，此处不可提前清理；
     * <p> 为避免长期运行导致临时文件堆积，每次写入前惰性回收超过保留时长的历史临时文件。
     *
     * @param fileInfo    文件信息
     * @param inputStream 文件输入流
     * @return 临时文件
     * @throws CommonException 通用异常
     */
    private File writeTempFile(FleaFileInfo fileInfo, InputStream inputStream) throws CommonException {
        File tempRoot = new File(System.getProperty(FleaFSConstants.SystemConstants.JAVA_IO_TMPDIR), TEMP_DIR_NAME);
        // 惰性清理历史临时文件
        cleanExpiredTempFiles(tempRoot);

        // 去除路径分隔符和Windows非法字符，避免路径穿越与非法文件名
        String fileName = sanitizeFileName(fileInfo.getFileName());
        File tempFile = new File(new File(tempRoot, RandomCode.toUUID()), fileName);
        FileUtils.copyInputStreamToFile(inputStream, tempFile);
        return tempFile;
    }

    /**
     * 清理已过期的下载临时文件（含子目录）
     *
     * @param tempRoot 下载临时文件根目录
     */
    private void cleanExpiredTempFiles(File tempRoot) {
        File[] tempFiles = tempRoot.listFiles();
        if (tempFiles == null || tempFiles.length == 0) return;

        long expireTime = DateUtils.getCurrentTime().getTime() - TEMP_FILE_KEEP_MILLIS;
        for (File tempFile : tempFiles) {
            if (tempFile.lastModified() < expireTime) {
                deleteQuietly(tempFile);
            }
        }
    }

    /**
     * 静默删除文件或目录（含子目录），删除失败仅记录日志
     *
     * @param file 待删除的文件或目录
     */
    private void deleteQuietly(File file) {
        if (ObjectUtils.isEmpty(file) || !file.exists()) return;

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    deleteQuietly(child);
                }
            }
        }

        if (!file.delete()) {
            LOGGER.error1(new Object() {}, "下载临时文件清理失败: {}", file.getAbsolutePath());
        }
    }

    /**
     * 去除文件名中的路径分隔符和Windows非法字符
     *
     * @param fileName 文件名
     * @return 安全文件名
     */
    private String sanitizeFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) return "download";
        return fileName.replaceAll("[/\\\\:*?\"<>|]", "_");
    }

    private void expireFleaTokenInfo(FleaTokenInfo fleaTokenInfo) {
        fleaTokenInfo.setExpiryDate(DateUtils.getCurrentTime());
        fleaTokenInfoSV.update(fleaTokenInfo);
    }
}
