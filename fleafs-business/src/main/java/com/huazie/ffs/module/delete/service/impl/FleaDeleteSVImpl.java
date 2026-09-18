package com.huazie.ffs.module.delete.service.impl;

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
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.ffs.common.util.FastDFSClient;
import com.huazie.ffs.module.auth.common.IFleaFileCategoryLocator;
import com.huazie.ffs.module.delete.service.interfaces.IFleaDeleteSV;
import com.huazie.ffs.module.search.service.interfaces.IFleaFileIndexSV;
import com.huazie.ffs.pojo.delete.input.InputDeleteAuthInfo;
import com.huazie.ffs.pojo.delete.input.InputFileDeleteInfo;
import com.huazie.ffs.pojo.delete.output.OutputDeleteAuthInfo;
import com.huazie.ffs.pojo.delete.output.OutputFileDeleteInfo;
import com.huazie.ffs.util.FleaFSUtils;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.common.exceptions.ServiceException;
import com.huazie.fleaframework.db.jpa.transaction.FleaTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Flea删除服务实现类，主要功能如下：
 * <p> 删除鉴权，用于获取文件删除所需的鉴权token（token会落库，并关联文件信息）
 * <p> 文件删除，采用逻辑删除方式，仅更新文件状态，FastDFS中的文件保留，
 * 待后续异步任务按保留期限（默认180天）执行物理删除
 * <p> 实现 {@link IFleaFileCategoryLocator}，为文件管理授权校验提供删除操作的文件类目编号。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaDeleteSVImpl implements IFleaDeleteSV, IFleaFileCategoryLocator {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaDeleteSVImpl.class);

    private IFleaFileInfoSV fleaFileInfoSV;

    private IFleaTokenInfoSV fleaTokenInfoSV;

    private IFleaFileAttrSV fleaFileAttrSV;

    private IFleaFileCategorySV fleaFileCategorySV;

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
    public OutputDeleteAuthInfo deleteAuth(InputDeleteAuthInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "删除鉴权业务入参");

        String fileId = input.getFileId();
        FleaFSCheck.checkBlank1(fileId, "文件编号(fileId)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "删除鉴权，fileId = {}", fileId);

        // 校验文件信息是否存在且可删除
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileDeletable(fileInfo, fileId);

        // 查询文件关联的类目编号【文件属性表中获取】
        Long categoryId = fleaFileAttrSV.queryFileCategoryId(fileId);

        // 获取文件类目，并校验文件类目是否存在【是否启用文件删除操作由过滤器统一校验】
        FleaFileCategory fleaFileCategory = fleaFileCategorySV.query(categoryId);
        FleaFSCheck.checkFleaFileCategory(fleaFileCategory, categoryId, null);

        // 生成删除鉴权token，为保证token和文件信息落在同一分库，token末位同文件编号末位
        String token = generateToken(fileId);

        // 生成token鉴权信息
        Date expiryDate = DateUtils.getTime(Calendar.MINUTE, FleaFSUtils.getTokenExpMinutes());
        Map<String, Object> extendMap = new HashMap<>();
        extendMap.put(FleaFSEntityConstants.E_EXPIRY_DATE, expiryDate);
        fleaTokenInfoSV.saveFleaTokenInfo(token, fileId, categoryId, OperateTypeEnum.DELETE, extendMap);

        LOGGER.debug1(obj, "删除鉴权，token = {}", token);

        OutputDeleteAuthInfo output = new OutputDeleteAuthInfo();
        output.setToken(token);

        return output;
    }

    @Override
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs", seq = "'SEQ=' + #input.token")
    public OutputFileDeleteInfo fileDelete(InputFileDeleteInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "文件删除业务入参");

        String token = input.getToken();
        FleaFSCheck.checkBlank1(token, "删除鉴权令牌(token)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "文件删除，token = {}", token);

        // 根据token查询有效的Flea鉴权信息
        FleaTokenInfo fleaTokenInfo = fleaTokenInfoSV.queryValidFleaTokenInfo(token);
        FleaFSCheck.checkFleaTokenInfo(fleaTokenInfo, token);

        // 获取Flea文件信息
        String fileId = fleaTokenInfo.getFileId();
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileDeletable(fileInfo, fileId);

        // 逻辑删除文件信息【FastDFS中的文件保留，待后续按保留期限物理删除】
        fileInfo.setFileState(FileStateEnum.FILE_LOGIC_DELETED.getState());
        fileInfo.setDoneDate(DateUtils.getCurrentTime());
        fleaFileInfoSV.update(fileInfo);

        LOGGER.debug1(obj, "文件删除，fileId = {}", fileId);

        // 失效Flea鉴权信息
        expireFleaTokenInfo(fleaTokenInfo);

        OutputFileDeleteInfo output = new OutputFileDeleteInfo();
        output.setFileId(fileId);

        return output;
    }

    /**
     * 生成删除鉴权token，并保证token末位与文件编号末位相同
     * <p> 文件编号末位同token末位，可保证删除鉴权信息和文件信息落在同一个分库
     *
     * @param fileId 文件编号
     * @return 删除鉴权token
     * @since 1.0.0
     */
    private String generateToken(String fileId) {
        String token = fleaTokenInfoSV.generateToken();
        String last = StringUtils.subStrLast(fileId, 1);
        return StringUtils.strCat(token.substring(0, token.length() - 1), last);
    }

    @Override
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs", seq = "'SEQ=' + #fileId")
    public void physicalDeleteFile(String fileId) throws CommonException {
        FleaFSCheck.checkBlank1(fileId, "文件编号(fileId)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "文件物理删除，fileId = {}", fileId);

        // 查询文件信息
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileInfo(fileInfo, fileId);

        // 仅允许针对已逻辑删除的文件执行物理删除
        if (FileStateEnum.FILE_LOGIC_DELETED.getState() != fileInfo.getFileState()) {
            // ERROR-SERVICE0000000013 文件【{0}】未逻辑删除，不允许物理删除！
            throw new ServiceException("ERROR-SERVICE0000000013", fileId);
        }

        // 删除FastDFS中的文件【fastdfs_id即文件完整ID】
        String fastdfsId = fileInfo.getFastdfsId();
        if (StringUtils.isNotBlank(fastdfsId)) {
            FastDFSClient.deleteFile(fastdfsId);
        }

        // 清空文件存储信息【文件记录保留用于审计】
        fileInfo.setFastdfsId("");
        fileInfo.setSecretKey("");
        fileInfo.setDoneDate(DateUtils.getCurrentTime());
        fleaFileInfoSV.update(fileInfo);

        // 删除文件ES索引【容错处理，ES不可用不阻断主链路】
        fleaFileIndexSV.removeFleaFileInfo(fileId);

        LOGGER.debug1(obj, "文件物理删除完成，fileId = {}", fileId);
    }

    private void expireFleaTokenInfo(FleaTokenInfo fleaTokenInfo) {
        fleaTokenInfo.setExpiryDate(DateUtils.getCurrentTime());
        fleaTokenInfoSV.update(fleaTokenInfo);
    }

    @Override
    public OperateTypeEnum getOperateType() {
        return OperateTypeEnum.DELETE;
    }

    @Override
    public Long getCategoryId(Object inputObj) throws CommonException {
        // 删除鉴权：业务入参携带文件编号，由文件属性中取文件类目编号
        if (inputObj instanceof InputDeleteAuthInfo) {
            return fleaFileAttrSV.queryFileCategoryId(((InputDeleteAuthInfo) inputObj).getFileId());
        }
        // 文件删除：业务入参携带鉴权令牌，由令牌关联的鉴权信息中取文件类目编号
        if (inputObj instanceof InputFileDeleteInfo) {
            String token = ((InputFileDeleteInfo) inputObj).getToken();
            FleaTokenInfo fleaTokenInfo = fleaTokenInfoSV.queryValidFleaTokenInfo(token);
            return ObjectUtils.isEmpty(fleaTokenInfo) ? null : fleaTokenInfo.getCategoryId();
        }
        return null;
    }
}
