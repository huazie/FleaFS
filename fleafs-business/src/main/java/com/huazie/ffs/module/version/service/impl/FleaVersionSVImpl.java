package com.huazie.ffs.module.version.service.impl;

import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.ffs.base.service.interfaces.IFleaFileInfoSV;
import com.huazie.ffs.base.service.interfaces.IFleaFileVersionSV;
import com.huazie.ffs.base.util.FleaFSCheck;
import com.huazie.ffs.module.version.service.interfaces.IFleaVersionSV;
import com.huazie.ffs.pojo.version.input.InputFileVersionInfo;
import com.huazie.ffs.pojo.version.output.OutputFileVersionInfo;
import com.huazie.ffs.pojo.version.output.OutputFileVersionItem;
import com.huazie.fleaframework.common.DateFormatEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.db.jpa.transaction.FleaTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Flea版本服务实现类，主要功能如下：
 * <p> 版本查询，用于查询指定文件的历史版本列表
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaVersionSVImpl implements IFleaVersionSV {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaVersionSVImpl.class);

    private IFleaFileInfoSV fleaFileInfoSV;

    private IFleaFileVersionSV fleaFileVersionSV;

    @Autowired
    @Qualifier("fleaFileInfoSV")
    public void setFleaFileInfoSV(IFleaFileInfoSV fleaFileInfoSV) {
        this.fleaFileInfoSV = fleaFileInfoSV;
    }

    @Autowired
    @Qualifier("fleaFileVersionSV")
    public void setFleaFileVersionSV(IFleaFileVersionSV fleaFileVersionSV) {
        this.fleaFileVersionSV = fleaFileVersionSV;
    }

    @Override
    @FleaTransactional(value = "fleaFSTransactionManager", unitName = "fleafs", seq = "'SEQ=' + #input.fileId")
    public OutputFileVersionInfo queryFileVersions(InputFileVersionInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "版本查询业务入参");

        String fileId = input.getFileId();
        FleaFSCheck.checkBlank1(fileId, "文件编号(fileId)");

        Object obj = new Object() {};
        LOGGER.debug1(obj, "版本查询，fileId = {}", fileId);

        // 校验文件信息是否存在
        FleaFileInfo fileInfo = fleaFileInfoSV.query(fileId);
        FleaFSCheck.checkFleaFileInfo(fileInfo, fileId);

        // 查询文件的历史版本列表【按版本编号倒序】
        List<FleaFileVersion> fleaFileVersions = fleaFileVersionSV.queryFleaFileVersions(fileId);

        List<OutputFileVersionItem> versionList = new ArrayList<>();
        for (FleaFileVersion fleaFileVersion : fleaFileVersions) {
            versionList.add(convertFileVersion(fleaFileVersion));
        }

        OutputFileVersionInfo output = new OutputFileVersionInfo();
        output.setFileId(fileId);
        output.setVersionCount(versionList.size());
        output.setVersionList(versionList);

        LOGGER.debug1(obj, "版本查询，versionCount = {}", versionList.size());

        return output;
    }

    /**
     * 将文件版本实体转换为版本明细业务出参
     *
     * @param fleaFileVersion 文件版本实体
     * @return 版本明细业务出参
     * @since 1.0.0
     */
    private OutputFileVersionItem convertFileVersion(FleaFileVersion fleaFileVersion) {
        OutputFileVersionItem item = new OutputFileVersionItem();
        item.setVersionId(fleaFileVersion.getVersionId());
        item.setVersionCode(fleaFileVersion.getVersionCode());
        item.setVersionName(fleaFileVersion.getVersionName());
        item.setVersionDesc(fleaFileVersion.getVersionDesc());
        item.setFileName(fleaFileVersion.getFileName());
        item.setFileType(fleaFileVersion.getFileType());
        item.setFileSize(fleaFileVersion.getFileSize());
        item.setFileSizeDesc(fleaFileVersion.getFileSizeDesc());
        item.setCreateDate(DateUtils.date2String(fleaFileVersion.getCreateDate(), DateFormatEnum.YYYYMMDDHHMMSS));
        return item;
    }
}
