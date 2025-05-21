package com.huazie.ffs.base.service.impl;

import com.huazie.ffs.base.dao.interfaces.IFleaFileInfoDAO;
import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.service.interfaces.IFleaFileInfoSV;
import com.huazie.fleaframework.common.DateFormatEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.RandomCode;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.fleaframework.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Flea文件信息SV层实现类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaFileInfoSV")
public class FleaFileInfoSVImpl extends AbstractFleaJPASVImpl<FleaFileInfo> implements IFleaFileInfoSV {

    private IFleaFileInfoDAO fleaFileInfoDao;

    @Autowired
    @Qualifier("fleaFileInfoDAO")
    public void setFleaFileInfoDao(IFleaFileInfoDAO fleaFileInfoDao) {
        this.fleaFileInfoDao = fleaFileInfoDao;
    }

    @Override
    public String preSaveFleaFileInfo(String token, String fileName, Map<String, Object> extendMap) throws CommonException {
        // 为了保证文件信息和token鉴权信息在同一个库，文件编号的最后一位需要同token的最后一位相同
        String last = StringUtils.subStrLast(token, 1);
        // 生成文件编号，保持文件编号同token的最后一位相同
        String fileId = StringUtils.strCat(generateFileId(), last);
        FleaFileInfo fleaFileInfo = new FleaFileInfo(fileId, fileName, "", "");
        this.fleaFileInfoDao.save(fleaFileInfo);
        return fileId;
    }

    @Override
    public String generateFileId() {
        String curDateStr = DateUtils.date2String(DateUtils.getCurrentTime(), DateFormatEnum.YYYYMMDDHHMMSS);
        return curDateStr + RandomCode.toUUID();
    }

    @Override
    protected IAbstractFleaJPADAO<FleaFileInfo> getDAO() {
        return fleaFileInfoDao;
    }
}