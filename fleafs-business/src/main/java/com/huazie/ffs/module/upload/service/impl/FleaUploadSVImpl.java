package com.huazie.ffs.module.upload.service.impl;

import com.huazie.ffs.base.FileStateEnum;
import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.base.service.interfaces.IFleaFileAttrSV;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
import com.huazie.ffs.base.service.interfaces.IFleaFileInfoSV;
import com.huazie.ffs.base.service.interfaces.IFleaFileVersionSV;
import com.huazie.ffs.base.service.interfaces.IFleaTokenInfoSV;
import com.huazie.ffs.base.util.FleaFSCheck;
import com.huazie.ffs.common.FileSizeUnitEnum;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.ffs.common.util.EncryptionUtils;
import com.huazie.ffs.common.util.FastDFSClient;
import com.huazie.ffs.common.util.FileUtils;
import com.huazie.ffs.module.auth.common.IFleaFileCategoryLocator;
import com.huazie.ffs.module.search.service.interfaces.IFleaFileIndexSV;
import com.huazie.ffs.module.upload.service.interfaces.IFleaUploadSV;
import com.huazie.ffs.pojo.upload.input.InputFileUploadInfo;
import com.huazie.ffs.pojo.upload.input.InputUploadAuthInfo;
import com.huazie.ffs.pojo.upload.output.OutputFileUploadInfo;
import com.huazie.ffs.pojo.upload.output.OutputUploadAuthInfo;
import com.huazie.ffs.util.FleaFSUtils;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.db.common.util.FleaLibUtil;
import com.huazie.fleaframework.db.jpa.transaction.FleaTransactional;
import com.huazie.fleaframework.jersey.common.FleaJerseyManager;
import com.huazie.fleaframework.jersey.common.data.FleaFileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Flea上传服务实现类，主要功能如下：
 * <p> 上传鉴权，用于获取文件上传所需的鉴权token
 * <p> 文件上传，用于实际文件的上传，内部通过FastDFS API操作文件上传
 * <p> 实现 {@link IFleaFileCategoryLocator}，为文件管理授权校验提供上传操作的文件类目编号。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaUploadSVImpl implements IFleaUploadSV, IFleaFileCategoryLocator {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaUploadSVImpl.class);

    private IFleaFileCategorySV fleaFileCategorySV;

    private IFleaTokenInfoSV fleaTokenInfoSV;

    private IFleaFileInfoSV fleaFileInfoSV;

    private IFleaFileAttrSV fleaFileAttrSV;

    private IFleaFileVersionSV fleaFileVersionSV;

    private IFleaFileIndexSV fleaFileIndexSV;

    @Autowired
    @Qualifier("fleaFileIndexSV")
    public void setFleaFileIndexSV(IFleaFileIndexSV fleaFileIndexSV) {
        this.fleaFileIndexSV = fleaFileIndexSV;
    }

    @Autowired
    @Qualifier("fleaFileCategorySV")
    public void setFleaFileCategorySV(IFleaFileCategorySV fleaFileCategorySV) {
        this.fleaFileCategorySV = fleaFileCategorySV;
    }

    @Autowired
    @Qualifier("fleaTokenInfoSV")
    public void setFleaTokenInfoSV(IFleaTokenInfoSV fleaTokenInfoSV) {
        this.fleaTokenInfoSV = fleaTokenInfoSV;
    }

    @Autowired
    @Qualifier("fleaFileInfoSV")
    public void setFleaFileInfoSV(IFleaFileInfoSV fleaFileInfoSV) {
        this.fleaFileInfoSV = fleaFileInfoSV;
    }

    @Autowired
    @Qualifier("fleaFileAttrSV")
    public void setFleaFileAttrSV(IFleaFileAttrSV fleaFileAttrSV) {
        this.fleaFileAttrSV = fleaFileAttrSV;
    }

    @Autowired
    @Qualifier("fleaFileVersionSV")
    public void setFleaFileVersionSV(IFleaFileVersionSV fleaFileVersionSV) {
        this.fleaFileVersionSV = fleaFileVersionSV;
    }

    @Override
    public void setSplitLibSequence() {
        // 生成Token
        String token = fleaTokenInfoSV.generateToken();
        // 设置分库序列集
        FleaLibUtil.setSplitLibSequence("SEQ", token);
    }

    @Override
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs",
            seqProvider = IFleaUploadSV.class, seqMethod = "setSplitLibSequence")
    public OutputUploadAuthInfo uploadAuth(InputUploadAuthInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "上传鉴权业务入参");

        // 生成Token
        String token = FleaLibUtil.getSplitLibSeqValue("SEQ", String.class);

        Object obj = new Object() {};
        LOGGER.debug1(obj, "上传鉴权，token = {}", token);

        FleaFSCheck.checkBlank1(input.getFileName(), FleaFSEntityConstants.FileInfoEntityConstants.E_FILE_NAME);

        Map<String, Object> extendMap = new HashMap<>();
        // 预生成文件信息
        String fileId = fleaFileInfoSV.preSaveFleaFileInfo(token, input.getFileName(), extendMap);

        LOGGER.debug1(obj, "上传鉴权，fileId = {}", fileId);

        // 获取Flea文件类目
        FleaFileCategory fleaFileCategory = fleaFileCategorySV.queryFleaCategory(input.getCategoryId(), input.getCategoryCode());
        FleaFSCheck.checkFleaFileCategory(fleaFileCategory, input.getCategoryId(), input.getCategoryCode());

        Long categoryId = fleaFileCategory.getCategoryId();
        // 预生成文件属性信息
        fleaFileAttrSV.preSavFileRelCategoryAttr(fileId, categoryId, extendMap);

        // 生成token鉴权信息
        Date expiryDate = DateUtils.getTime(Calendar.MINUTE, FleaFSUtils.getTokenExpMinutes());
        extendMap.put(FleaFSEntityConstants.E_EXPIRY_DATE, expiryDate);
        fleaTokenInfoSV.saveFleaTokenInfo(token, fileId, categoryId, OperateTypeEnum.UPLOAD, extendMap);

        OutputUploadAuthInfo output = new OutputUploadAuthInfo();
        output.setToken(token);

        return output;
    }

    @Override
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs", seq = "'SEQ=' + #input.token")
    public OutputFileUploadInfo fileUpload(InputFileUploadInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "文件上传业务入参");

        String token = input.getToken();
        FleaFSCheck.checkBlank1(token, "上传鉴权令牌(token)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "文件上传，token = {}", token);

        // 根据token查询有效的Flea鉴权信息
        FleaTokenInfo fleaTokenInfo = this.fleaTokenInfoSV.queryValidFleaTokenInfo(token);
        FleaFSCheck.checkFleaTokenInfo(fleaTokenInfo, token);

        // 获取文件对应的类目信息
        Long categoryId = fleaTokenInfo.getCategoryId();
        FleaFileCategory fleaFileCategory = fleaFileCategorySV.query(categoryId);
        FleaFSCheck.checkFleaFileCategory(fleaFileCategory, categoryId, null);

        // 获取Flea文件信息
        String fileId = fleaTokenInfo.getFileId();
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileInfo(fileInfo, fileId);

        LOGGER.debug1(obj, "文件上传，fileId = {}", fileId);

        // 获取上传的文件对象
        FleaFileObject fileObject = FleaJerseyManager.getManager().getFileObject();
        String fileName = fileObject.getFileName();
        File uploadFile = fileObject.getFile();

        // 获取文件类目限制的上传文件大小【单位：MB】
        Long maxFileSize = fleaFileCategory.getMaxFileSize();
        FleaFSCheck.checkFileSize(uploadFile, maxFileSize);

        // 校验类目配置的文件加密方式是否支持
        String encryptType = fleaFileCategory.getEncryptType();
        FleaFSCheck.checkEncryptionType(encryptType, categoryId);

        // 按需生成密钥并加密上传文件【密钥经Base64编码后随文件信息落库】
        String secretKey = null;
        File uploadTargetFile = uploadFile;
        if (EncryptionUtils.isEncryptionNeeded(encryptType)) {
            secretKey = EncryptionUtils.generateSecretKey(encryptType);
            uploadTargetFile = EncryptionUtils.encryptFile(uploadFile, encryptType, secretKey);
        }

        // 通过FastDFS工具类上传文件【如需加密，上传加密后的临时文件】
        String fastdfsId = FastDFSClient.uploadFile(uploadTargetFile, fileName);

        // 清理加密临时文件
        if (uploadTargetFile != uploadFile) {
            uploadTargetFile.delete();
        }

        LOGGER.debug1(obj, "文件上传，fileName = {}", fileName);
        LOGGER.debug1(obj, "文件上传，fastdfsId = {}", fastdfsId);

        // 更新文件信息【含版本快照留存】
        updateFleaFileInfo(fileInfo, fileName, fastdfsId, uploadFile, secretKey);

        // 同步文件ES索引【容错处理，ES不可用不阻断主链路】
        fleaFileIndexSV.indexFleaFileInfo(fileInfo);

        // 失效Flea鉴权信息
        expireFleaTokenInfo(fleaTokenInfo);

        OutputFileUploadInfo outputFileUploadInfo = new OutputFileUploadInfo();
        outputFileUploadInfo.setFileId(fileId);

        return outputFileUploadInfo;
    }

    /**
     * 更新文件信息，并留存文件版本快照
     *
     * @param fileInfo   文件信息
     * @param fileName   文件名称
     * @param fastdfsId  FastDFS文件编号
     * @param uploadFile 上传的文件
     * @param secretKey  密钥【Base64编码，无需加密时为null】
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private void updateFleaFileInfo(FleaFileInfo fileInfo, String fileName, String fastdfsId, File uploadFile, String secretKey) throws CommonException {
        fileInfo.setFileName(fileName);
        fileInfo.setFileState(FileStateEnum.FILE_IN_USE.getState());
        fileInfo.setFastdfsId(fastdfsId);
        fileInfo.setSecretKey(secretKey);
        // FastDFS中的文件访问路径（groupName/relativePath/filename）
        fileInfo.setFilePath(fastdfsId);
        // 文件大小【单位：B】，注意不能用 doubleToLongBits（会取IEEE754位模式，得到垃圾值）
        fileInfo.setFileSize(Math.round(FileUtils.getFileSize(uploadFile, FileSizeUnitEnum.BYTES)));
        fileInfo.setFileSizeDesc(FileUtils.getSmartFileSize(uploadFile));
        fileInfo.setDoneDate(DateUtils.getCurrentTime());

        // 留存文件版本快照，并回填当前文件版本编号
        FleaFileVersion fleaFileVersion = fleaFileVersionSV.saveFleaFileVersion(fileInfo, "文件上传");
        fileInfo.setFileVersionId(fleaFileVersion.getVersionId());

        fleaFileInfoSV.update(fileInfo);
    }

    private void expireFleaTokenInfo(FleaTokenInfo fleaTokenInfo) {
        fleaTokenInfo.setExpiryDate(DateUtils.getCurrentTime());
        fleaTokenInfoSV.update(fleaTokenInfo);
    }

    @Override
    public OperateTypeEnum getOperateType() {
        return OperateTypeEnum.UPLOAD;
    }

    @Override
    public Long getCategoryId(Object inputObj) throws CommonException {
        // 上传鉴权：业务入参直接携带文件类目编号
        if (inputObj instanceof InputUploadAuthInfo) {
            return ((InputUploadAuthInfo) inputObj).getCategoryId();
        }
        // 文件上传：业务入参携带鉴权令牌，由令牌关联的鉴权信息中取文件类目编号
        if (inputObj instanceof InputFileUploadInfo) {
            String token = ((InputFileUploadInfo) inputObj).getToken();
            FleaTokenInfo fleaTokenInfo = fleaTokenInfoSV.queryValidFleaTokenInfo(token);
            return ObjectUtils.isEmpty(fleaTokenInfo) ? null : fleaTokenInfo.getCategoryId();
        }
        return null;
    }
}
