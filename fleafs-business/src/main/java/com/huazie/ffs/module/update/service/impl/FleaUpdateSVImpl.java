package com.huazie.ffs.module.update.service.impl;

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
import com.huazie.ffs.module.search.service.interfaces.IFleaFileIndexSV;
import com.huazie.ffs.module.update.service.interfaces.IFleaUpdateSV;
import com.huazie.ffs.pojo.update.input.InputFileUpdateInfo;
import com.huazie.ffs.pojo.update.input.InputUpdateAuthInfo;
import com.huazie.ffs.pojo.update.output.OutputFileUpdateInfo;
import com.huazie.ffs.pojo.update.output.OutputUpdateAuthInfo;
import com.huazie.ffs.util.FleaFSUtils;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.StringUtils;
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
 * Flea更新服务实现类，主要功能如下：
 * <p> 更新鉴权，用于获取文件更新所需的鉴权token（token会落库，并关联文件信息）
 * <p> 文件更新，用于实际文件的更新，内部通过FastDFS API操作文件上传，并留存文件版本快照
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaUpdateSVImpl implements IFleaUpdateSV {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaUpdateSVImpl.class);

    private IFleaFileInfoSV fleaFileInfoSV;

    private IFleaFileCategorySV fleaFileCategorySV;

    private IFleaTokenInfoSV fleaTokenInfoSV;

    private IFleaFileAttrSV fleaFileAttrSV;

    private IFleaFileVersionSV fleaFileVersionSV;

    private IFleaFileIndexSV fleaFileIndexSV;

    @Autowired
    @Qualifier("fleaFileIndexSV")
    public void setFleaFileIndexSV(IFleaFileIndexSV fleaFileIndexSV) {
        this.fleaFileIndexSV = fleaFileIndexSV;
    }

    @Autowired
    @Qualifier("fleaFileInfoSV")
    public void setFleaFileInfoSV(IFleaFileInfoSV fleaFileInfoSV) {
        this.fleaFileInfoSV = fleaFileInfoSV;
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
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs", seq = "'SEQ=' + #input.fileId")
    public OutputUpdateAuthInfo updateAuth(InputUpdateAuthInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "更新鉴权业务入参");

        String fileId = input.getFileId();
        FleaFSCheck.checkBlank1(fileId, "文件编号(fileId)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "更新鉴权，fileId = {}", fileId);

        // 校验文件信息是否存在且可更新
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileUpdatable(fileInfo, fileId);

        // 查询文件关联的类目编号【文件属性表中获取】
        Long categoryId = fleaFileAttrSV.queryFileCategoryId(fileId);

        // 获取文件类目，并校验类目是否启用文件更新操作
        FleaFileCategory fleaFileCategory = fleaFileCategorySV.query(categoryId);
        FleaFSCheck.checkFleaFileCategory(fleaFileCategory, categoryId, null);
        FleaFSCheck.checkOperationState(fleaFileCategory, OperateTypeEnum.UPDATE, categoryId);

        // 生成更新鉴权token，为保证token和文件信息落在同一分库，token末位同文件编号末位
        String token = generateToken(fileId);

        // 生成token鉴权信息
        Date expiryDate = DateUtils.getTime(Calendar.MINUTE, FleaFSUtils.getTokenExpMinutes());
        Map<String, Object> extendMap = new HashMap<>();
        extendMap.put(FleaFSEntityConstants.E_EXPIRY_DATE, expiryDate);
        fleaTokenInfoSV.saveFleaTokenInfo(token, fileId, categoryId, OperateTypeEnum.UPDATE, extendMap);

        LOGGER.debug1(obj, "更新鉴权，token = {}", token);

        OutputUpdateAuthInfo output = new OutputUpdateAuthInfo();
        output.setToken(token);

        return output;
    }

    @Override
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs", seq = "'SEQ=' + #input.token")
    public OutputFileUpdateInfo fileUpdate(InputFileUpdateInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "文件更新业务入参");

        String token = input.getToken();
        FleaFSCheck.checkBlank1(token, "更新鉴权令牌(token)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "文件更新，token = {}", token);

        // 根据token查询有效的Flea鉴权信息
        FleaTokenInfo fleaTokenInfo = fleaTokenInfoSV.queryValidFleaTokenInfo(token);
        FleaFSCheck.checkFleaTokenInfo(fleaTokenInfo, token);

        // 获取文件对应的类目信息
        Long categoryId = fleaTokenInfo.getCategoryId();
        FleaFileCategory fleaFileCategory = fleaFileCategorySV.query(categoryId);
        FleaFSCheck.checkFleaFileCategory(fleaFileCategory, categoryId, null);

        // 获取Flea文件信息
        String fileId = fleaTokenInfo.getFileId();
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileUpdatable(fileInfo, fileId);

        // 获取更新的文件对象
        FleaFileObject fileObject = FleaJerseyManager.getManager().getFileObject();
        String fileName = fileObject.getFileName();
        File uploadFile = fileObject.getFile();

        // 获取文件类目限制的上传文件大小【单位：MB】
        Long maxFileSize = fleaFileCategory.getMaxFileSize();
        FleaFSCheck.checkFileSize(uploadFile, maxFileSize);

        // 校验类目配置的文件加密方式是否支持
        String encryptType = fleaFileCategory.getEncryptType();
        FleaFSCheck.checkEncryptionType(encryptType, categoryId);

        // 按需生成新密钥并加密上传文件【每次更新生成新密钥，版本快照各自持有，保证历史版本可解密】
        String secretKey = null;
        File uploadTargetFile = uploadFile;
        if (EncryptionUtils.isEncryptionNeeded(encryptType)) {
            secretKey = EncryptionUtils.generateSecretKey(encryptType);
            uploadTargetFile = EncryptionUtils.encryptFile(uploadFile, encryptType, secretKey);
        }

        // 通过FastDFS工具类上传新的文件【旧版本文件保留，供历史版本回溯；如需加密，上传加密后的临时文件】
        String fastdfsId = FastDFSClient.uploadFile(uploadTargetFile, fileName);

        // 清理加密临时文件
        if (uploadTargetFile != uploadFile) {
            uploadTargetFile.delete();
        }

        LOGGER.debug1(obj, "文件更新，fileId = {}", fileId);
        LOGGER.debug1(obj, "文件更新，fileName = {}", fileName);
        LOGGER.debug1(obj, "文件更新，fastdfsId = {}", fastdfsId);

        // 更新文件信息【含新版本快照留存】
        String versionCode = updateFleaFileInfo(fileInfo, fileName, fastdfsId, uploadFile, secretKey);

        // 同步文件ES索引【容错处理，ES不可用不阻断主链路】
        fleaFileIndexSV.indexFleaFileInfo(fileInfo);

        // 失效Flea鉴权信息
        expireFleaTokenInfo(fleaTokenInfo);

        OutputFileUpdateInfo output = new OutputFileUpdateInfo();
        output.setFileId(fileId);
        output.setVersionCode(versionCode);

        return output;
    }

    /**
     * 更新文件信息，并留存新的文件版本快照
     *
     * @param fileInfo   文件信息
     * @param fileName   文件名称
     * @param fastdfsId  FastDFS文件编号
     * @param uploadFile 上传的文件
     * @param secretKey  密钥【Base64编码，无需加密时为null】
     * @return 本次更新的版本编码
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private String updateFleaFileInfo(FleaFileInfo fileInfo, String fileName, String fastdfsId, File uploadFile, String secretKey) throws CommonException {
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

        // 留存新的文件版本快照，并回填当前文件版本编号
        FleaFileVersion fleaFileVersion = fleaFileVersionSV.saveFleaFileVersion(fileInfo, "文件更新");
        fileInfo.setFileVersionId(fleaFileVersion.getVersionId());

        fleaFileInfoSV.update(fileInfo);

        return fleaFileVersion.getVersionCode();
    }

    /**
     * 生成更新鉴权token，并保证token末位与文件编号末位相同
     * <p> 文件编号末位同token末位，可保证更新鉴权信息和文件信息落在同一个分库
     *
     * @param fileId 文件编号
     * @return 更新鉴权token
     * @since 1.0.0
     */
    private String generateToken(String fileId) {
        String token = fleaTokenInfoSV.generateToken();
        String last = StringUtils.subStrLast(fileId, 1);
        return StringUtils.strCat(token.substring(0, token.length() - 1), last);
    }

    private void expireFleaTokenInfo(FleaTokenInfo fleaTokenInfo) {
        fleaTokenInfo.setExpiryDate(DateUtils.getCurrentTime());
        fleaTokenInfoSV.update(fleaTokenInfo);
    }
}
