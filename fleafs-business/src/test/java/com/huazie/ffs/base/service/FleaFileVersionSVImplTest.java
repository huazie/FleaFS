package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaFileVersion;
import com.huazie.ffs.base.service.interfaces.IFleaFileVersionSV;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FleaFileVersionSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileVersionSVImplTest.class);

    @Resource(name = "fleaFileVersionSV")
    private IFleaFileVersionSV fleaFileVersionSV;

    @Test
    public void insertFleaFileVersion() throws CommonException {
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
        fleaFileVersion.setSystemUserId(1000L);
        fleaFileVersion.setState(1);
        fleaFileVersion.setCreateDate(DateUtils.getCurrentTime());
        fleaFileVersion.setRemarks("新增文件记录");

        fleaFileVersionSV.save(fleaFileVersion);
    }

    @Test
    public void queryFileVersion() throws CommonException {
        FleaFileVersion fleaFileVersion = new FleaFileVersion();
        fleaFileVersion.setVersionCode("v1.0.0");
        fleaFileVersion.setFileId("E120EA280AA50693D5568D0071456400");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("versionCode");
        List<FleaFileVersion> fleaFileVersionList =  fleaFileVersionSV.query(attrNames, fleaFileVersion);
        LOGGER.debug("FleaFileVersions = {}", fleaFileVersionList);
    }
}