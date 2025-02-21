package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.base.service.interfaces.IFleaFileInfoSV;
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
public class FleaFileInfoSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileInfoSVImplTest.class);

    @Resource(name = "fleaFileInfoSV")
    private IFleaFileInfoSV fleaFileInfoSV;

    @Test
    public void insertFleaFileInfo() throws CommonException {
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
        fileInfo.setSystemUserId(1000L);
        fileInfo.setState(1);
        fileInfo.setCreateDate(DateUtils.getCurrentTime());
        fileInfo.setRemarks("新增文件记录");

        fleaFileInfoSV.save(fileInfo);
    }

    @Test
    public void queryFileInfo() throws CommonException {
        FleaFileInfo fileInfo = new FleaFileInfo();
        fileInfo.setFileId("E120EA280AA50693D5568D0071456400");
        fileInfo.setFileName("自测.png");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("fileName");
        List<FleaFileInfo> fleaFileInfoList =  fleaFileInfoSV.query(attrNames, fileInfo);
        LOGGER.debug("FleaFileInfos = {}", fleaFileInfoList);
    }
}