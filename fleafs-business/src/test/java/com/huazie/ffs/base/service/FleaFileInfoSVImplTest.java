package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.service.interfaces.IFleaFileInfoSV;
import com.huazie.fleaframework.common.exception.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFileInfoSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileInfoSVImplTest.class);

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LOGGER.debug("ApplicationContext={}", applicationContext);
    }

    @Test
    public void insertFleaFileInfo() {
        try {
            IFleaFileInfoSV fleaFileInfoSV = (IFleaFileInfoSV) applicationContext.getBean("fleaFileInfoSV");
            FleaFileInfo fileInfo = new FleaFileInfo();
            fileInfo.setFileId("E120EA280AA50693D5568D0071456400");
            fileInfo.setFileName("自测.png");
            fileInfo.setFilePath("E://xxxx");
            fileInfo.setFileType("png");
            fileInfo.setFileSize(1024000L);
            fileInfo.setFileSizeDesc("1M");
            fileInfo.setFileVersionId(1L); // TODO
            fileInfo.setFileState(1);
            fileInfo.setFastdfsId("dfasdfasdfa/sdfadfaf_adfafawe");
            fileInfo.setUserId(10000L);
            fileInfo.setSystemId(1000L);
            fileInfo.setState(1);
            fileInfo.setCreateDate(DateUtils.getCurrentTime());
            fileInfo.setRemarks("新增文件记录");

            fleaFileInfoSV.save(fileInfo);
        } catch (CommonException e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void queryFileInfo() throws CommonException {
        IFleaFileInfoSV fleaFileInfoSV = (IFleaFileInfoSV) applicationContext.getBean("fleaFileInfoSV");
        FleaFileInfo fileInfo = new FleaFileInfo();
        fileInfo.setFileId("E120EA280AA50693D5568D0071456400");
        fileInfo.setFileName("自测.png");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("fileName");
        List<FleaFileInfo> fleaFileInfoList =  fleaFileInfoSV.query(attrNames, fileInfo);
        LOGGER.debug("FleaFileInfos = {}", fleaFileInfoList);
    }
}