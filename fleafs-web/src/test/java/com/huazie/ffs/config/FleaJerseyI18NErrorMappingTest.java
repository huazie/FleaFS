package com.huazie.ffs.config;

import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.core.base.cfgdata.entity.FleaJerseyI18nErrorMapping;
import com.huazie.fleaframework.core.base.cfgdata.service.interfaces.IFleaJerseyI18nErrorMappingSV;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * FleaJersey国际码和错误码映射单元测试类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FleaJerseyI18NErrorMappingTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaJerseyI18NErrorMappingTest.class);

    @Autowired
    @Qualifier("i18nErrorMappingSV")
    private IFleaJerseyI18nErrorMappingSV fleaJerseyI18nErrorMappingSV;

    @Test
    public void testInsertMapping() throws CommonException {
        List<FleaJerseyI18nErrorMapping> list = new ArrayList<>();

        FleaJerseyI18nErrorMapping mapping1 = new FleaJerseyI18nErrorMapping();
        mapping1.setResourceCode("upload");
        mapping1.setServiceCode("FLEA_SERVICE_UPLOAD_AUTH");
        mapping1.setI18nCode("ERROR-SERVICE0000000000");
        mapping1.setErrorCode("101000");
        mapping1.setReturnMess("");
        mapping1.setState(EntityStateEnum.IN_USE.getState());
        mapping1.setCreateDate(DateUtils.getCurrentTime());

        FleaJerseyI18nErrorMapping mapping2 = new FleaJerseyI18nErrorMapping();
        mapping2.setResourceCode("upload");
        mapping2.setServiceCode("FLEA_SERVICE_UPLOAD_AUTH");
        mapping2.setI18nCode("ERROR-SERVICE0000000001");
        mapping2.setErrorCode("101001");
        mapping2.setReturnMess("");
        mapping2.setState(EntityStateEnum.IN_USE.getState());
        mapping2.setCreateDate(DateUtils.getCurrentTime());

        FleaJerseyI18nErrorMapping mapping3 = new FleaJerseyI18nErrorMapping();
        mapping3.setResourceCode("upload");
        mapping3.setServiceCode("FLEA_SERVICE_UPLOAD_AUTH");
        mapping3.setI18nCode("ERROR-SERVICE0000000002");
        mapping3.setErrorCode("101002");
        mapping3.setReturnMess("");
        mapping3.setState(EntityStateEnum.IN_USE.getState());
        mapping3.setCreateDate(DateUtils.getCurrentTime());

        FleaJerseyI18nErrorMapping mapping4 = new FleaJerseyI18nErrorMapping();
        mapping4.setResourceCode("upload");
        mapping4.setServiceCode("FLEA_SERVICE_UPLOAD_AUTH");
        mapping4.setI18nCode("ERROR-SERVICE0000000003");
        mapping4.setErrorCode("101003");
        mapping4.setReturnMess("");
        mapping4.setState(EntityStateEnum.IN_USE.getState());
        mapping4.setCreateDate(DateUtils.getCurrentTime());

        list.add(mapping1);
        list.add(mapping2);
        list.add(mapping3);
        list.add(mapping4);

        fleaJerseyI18nErrorMappingSV.batchSave(list);
    }

    @Test
    public void testGetMappings() throws CommonException {
        fleaJerseyI18nErrorMappingSV.getMappings("upload", "FLEA_SERVICE_UPLOAD_AUTH");
    }

    @Test
    public void testGetMapping() throws CommonException {
        fleaJerseyI18nErrorMappingSV.getMapping("upload", "FLEA_SERVICE_UPLOAD_AUTH", "ERROR-SERVICE0000000003");
    }
}
