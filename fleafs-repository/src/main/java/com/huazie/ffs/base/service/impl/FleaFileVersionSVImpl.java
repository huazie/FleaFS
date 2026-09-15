package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileVersionDAO;
import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.ffs.base.service.interfaces.IFleaFileVersionSV;
import com.huazie.ffs.base.util.FleaFSCheck;
import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Flea文件版本SV层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaFileVersionSV")
public class FleaFileVersionSVImpl extends AbstractFleaJPASVImpl<FleaFileVersion> implements IFleaFileVersionSV {

    private IFleaFileVersionDAO fleaFileVersionDao;

    @Autowired
    @Qualifier("fleaFileVersionDAO")
    public void setFleaFileVersionDao(IFleaFileVersionDAO fleaFileVersionDao) {
        this.fleaFileVersionDao = fleaFileVersionDao;
    }

    @Override
    public FleaFileVersion saveFleaFileVersion(FleaFileInfo fileInfo, String versionDesc) throws CommonException {
        FleaFSCheck.checkEmpty(fileInfo, "文件信息");

        String fileId = fileInfo.getFileId();
        // 版本编码按文件已有版本数递增，如 V1、V2
        int versionNum = fleaFileVersionDao.queryFleaFileVersions(fileId).size() + 1;
        String versionCode = StringUtils.strCat(FleaFSConstants.FileVersionConstants.VERSION_CODE_PREFIX, StringUtils.valueOf(versionNum));

        // 留存当前文件的版本快照
        FleaFileVersion fleaFileVersion = new FleaFileVersion(versionCode, versionCode, fileId,
                fileInfo.getFileName(), fileInfo.getFilePath(), fileInfo.getFileType(), fileInfo.getFileSize(),
                fileInfo.getFileSizeDesc(), fileInfo.getFileState(), fileInfo.getFastdfsId(),
                fileInfo.getSecretKey(), versionDesc);
        this.save(fleaFileVersion);

        return fleaFileVersion;
    }

    @Override
    public List<FleaFileVersion> queryFleaFileVersions(String fileId) throws CommonException {
        return fleaFileVersionDao.queryFleaFileVersions(fileId);
    }

    @Override
    public FleaFileVersion queryLastFleaFileVersion(String fileId) throws CommonException {
        return fleaFileVersionDao.queryLastFleaFileVersion(fileId);
    }

    @Override
    protected IAbstractFleaJPADAO<FleaFileVersion> getDAO() {
        return fleaFileVersionDao;
    }
}