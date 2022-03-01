package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.ffs.base.service.interfaces.IFleaFileVersionSV;
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
public class FleaFileVersionSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileVersionSVImplTest.class);

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LOGGER.debug("ApplicationContext={}", applicationContext);
    }

    @Test
    public void insertFleaFileVersion() {
        try {
            IFleaFileVersionSV fleaFileInfoSV = (IFleaFileVersionSV) applicationContext.getBean("fleaFileVersionSV");
            FleaFileVersion fleaFileVersion = new FleaFileVersion();
            fleaFileVersion.setVersionCode("v1.0.0");
            fleaFileVersion.setVersionName("初始版本");
            fleaFileVersion.setVersionDesc("初始版本");
            fleaFileVersion.setFileId("E120EA280AA50693D5568D0071456400");
            fleaFileVersion.setFileName("自测.png");
            fleaFileVersion.setFilePath("E://xxxx");
            fleaFileVersion.setFileType("png");
            fleaFileVersion.setFileSize(1024000L);
            fleaFileVersion.setFileSizeDesc("1M");
            fleaFileVersion.setFileState(1);
            fleaFileVersion.setFastdfsId("dfasdfasdfa/sdfadfaf_adfafawe");
            fleaFileVersion.setUserId(10000L);
            fleaFileVersion.setSystemId(1000L);
            fleaFileVersion.setState(1);
            fleaFileVersion.setCreateDate(DateUtils.getCurrentTime());
            fleaFileVersion.setRemarks("新增文件记录");

            fleaFileInfoSV.save(fleaFileVersion);
        } catch (CommonException e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void queryFileVersion() throws CommonException {
        IFleaFileVersionSV fleaFileVersionSV = (IFleaFileVersionSV) applicationContext.getBean("fleaFileVersionSV");
        FleaFileVersion fleaFileVersion = new FleaFileVersion();
        fleaFileVersion.setVersionCode("v1.0.0");
        fleaFileVersion.setFileId("E120EA280AA50693D5568D0071456400");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("versionCode");
        List<FleaFileVersion> fleaFileVersionList =  fleaFileVersionSV.query(attrNames, fleaFileVersion);
        LOGGER.debug("FleaFileVersions = {}", fleaFileVersionList);
    }
}