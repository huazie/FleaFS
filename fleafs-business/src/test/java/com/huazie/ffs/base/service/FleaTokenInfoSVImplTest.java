package com.huazie.ffs.base.service;

import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.base.service.interfaces.IFleaTokenInfoSV;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.RandomCode;
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
public class FleaTokenInfoSVImplTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaTokenInfoSVImplTest.class);

    @Resource(name = "fleaTokenInfoSV")
    private IFleaTokenInfoSV fleaTokenInfoSV;

    @Test
    public void insertFleaTokenInfo() throws CommonException {
        FleaTokenInfo fleaTokenInfo = new FleaTokenInfo();
        fleaTokenInfo.setTokenId(RandomCode.toUUID());
        fleaTokenInfo.setFileId("E120EA280AA50693D5568D0071456400");
        fleaTokenInfo.setOperationType(OperateTypeEnum.UPLOAD.getType());
        fleaTokenInfo.setState(EntityStateEnum.IN_USE.getState());
        fleaTokenInfo.setUserId(10000L);
        fleaTokenInfo.setSystemUserId(1000L);
        fleaTokenInfo.setCreateDate(DateUtils.getCurrentTime());
        fleaTokenInfo.setEffectiveDate(DateUtils.getCurrentTime());
        fleaTokenInfo.setExpiryDate(DateUtils.getExpiryTimeForever());
        fleaTokenInfo.setRemarks("测试");

        fleaTokenInfoSV.save(fleaTokenInfo);
    }

    @Test
    public void queryFileTokenInfo() throws CommonException {
        FleaTokenInfo fleaTokenInfo = new FleaTokenInfo();
        fleaTokenInfo.setTokenId("3b69ad0f60cd4927aa6d7dc35b99a366");
        fleaTokenInfo.setFileId("E120EA280AA50693D5568D0071456400");

        Set<String> attrNames = new HashSet<>();
        attrNames.add("tokenId");
        List<FleaTokenInfo> fleaTokenInfoList =  fleaTokenInfoSV.query(attrNames, fleaTokenInfo);
        LOGGER.debug("FleaFileVersions = {}", fleaTokenInfoList);
    }
}