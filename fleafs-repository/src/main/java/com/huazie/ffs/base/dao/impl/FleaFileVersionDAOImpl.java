package com.huazie.ffs.base.dao.impl;

import com.huazie.ffs.base.FleaFSEntityConstants;
import com.huazie.ffs.base.dao.interfaces.IFleaFileVersionDAO;
import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.db.common.DBConstants;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Flea文件版本DAO层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("fleaFileVersionDAO")
public class FleaFileVersionDAOImpl extends FleaFSDAOImpl<FleaFileVersion> implements IFleaFileVersionDAO {

    @Override
    public List<FleaFileVersion> queryFleaFileVersions(String fileId) throws CommonException {
        FleaFileVersion fleaFileVersion = new FleaFileVersion();
        fleaFileVersion.setFileId(fileId);
        fleaFileVersion.setState(EntityStateEnum.IN_USE.getState());

        return this.getQuery(null)
                .initQueryEntity(fleaFileVersion)
                .equal(FleaFSEntityConstants.FileVersionEntityConstants.E_FILE_ID)
                .equal(FleaFSEntityConstants.E_STATE)
                .addOrderBy(FleaFSEntityConstants.FileVersionEntityConstants.E_VERSION_ID, DBConstants.SQLConstants.SQL_ORDER_DESC)
                .getResultList();
    }

    @Override
    public FleaFileVersion queryLastFleaFileVersion(String fileId) throws CommonException {
        List<FleaFileVersion> fleaFileVersions = queryFleaFileVersions(fileId);
        return CollectionUtils.getFirstElement(fleaFileVersions, FleaFileVersion.class);
    }
}