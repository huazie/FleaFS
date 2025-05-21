package com.huazie.ffs.module.upload.service.impl;

import com.huazie.ffs.base.FileStateEnum;
import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.entity.FleaFileCategory;
import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.base.service.interfaces.IFleaFileAttrSV;
import com.huazie.ffs.base.service.interfaces.IFleaFileCategorySV;
import com.huazie.ffs.base.service.interfaces.IFleaFileInfoSV;
import com.huazie.ffs.base.service.interfaces.IFleaTokenInfoSV;
import com.huazie.ffs.base.util.FleaFSCheck;
import com.huazie.ffs.common.FileSizeUnitEnum;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.ffs.common.util.FastDFSClient;
import com.huazie.ffs.common.util.FileUtils;
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
 * Flea上传服务实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaUploadSVImpl implements IFleaUploadSV {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaUploadSVImpl.class);

    private IFleaFileCategorySV fleaFileCategorySV;

    private IFleaTokenInfoSV fleaTokenInfoSV;

    private IFleaFileInfoSV fleaFileInfoSV;

    private IFleaFileAttrSV fleaFileAttrSV;

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

        FleaFSCheck.checkBlank1(input.getFileName(), FleaFSEntityConstants.FileInfoEntityConstants.E_FILE_NAME);

        Map<String, Object> extendMap = new HashMap<>();
        // 预生成文件信息
        String fileId = fleaFileInfoSV.preSaveFleaFileInfo(token, input.getFileName(), extendMap);

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

        // 获取上传的文件对象
        FleaFileObject fileObject = FleaJerseyManager.getManager().getFileObject();
        String fileName = fileObject.getFileName();
        File uploadFile = fileObject.getFile();

        // 获取文件类目限制的上传文件大小【单位：MB】
        Long maxFileSize = fleaFileCategory.getMaxFileSize();
        FleaFSCheck.checkFileSize(uploadFile, maxFileSize);

        // 通过FastDFS工具类上传文件
        String fastdfsId = FastDFSClient.uploadFile(uploadFile, fileName);

        // 更新文件信息
        updateFleaFileInfo(fileInfo, fileName, fastdfsId, uploadFile);

        // 失效Flea鉴权信息
        expireFleaTokenInfo(fleaTokenInfo);

        OutputFileUploadInfo outputFileUploadInfo = new OutputFileUploadInfo();
        outputFileUploadInfo.setFileId(fileId);

        return outputFileUploadInfo;
    }

    private void updateFleaFileInfo(FleaFileInfo fileInfo, String fileName, String fastdfsId, File uploadFile) {
        fileInfo.setFileName(fileName);
        fileInfo.setFileState(FileStateEnum.FILE_IN_USE.getState());
        fileInfo.setFastdfsId(fastdfsId);
        fileInfo.setFileSize(Double.doubleToLongBits(FileUtils.getFileSize(uploadFile, FileSizeUnitEnum.BYTES)));
        fileInfo.setFileSizeDesc(FileUtils.getSmartFileSize(uploadFile));
        fileInfo.setDoneDate(DateUtils.getCurrentTime());
        fleaFileInfoSV.update(fileInfo);
    }

    private void expireFleaTokenInfo(FleaTokenInfo fleaTokenInfo) {
        fleaTokenInfo.setExpiryDate(DateUtils.getCurrentTime());
        fleaTokenInfoSV.update(fleaTokenInfo);
    }
}
