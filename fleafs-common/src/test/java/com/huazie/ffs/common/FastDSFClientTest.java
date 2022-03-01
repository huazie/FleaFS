package com.huazie.ffs.common;

import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import org.junit.Test;

/**
 * @author huazie
 * @version 1.1.0
 * @since 1.1.0
 */
public class FastDSFClientTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FastDSFClientTest.class);

    @Test
    public void test() {
        long operatorCode = 00100100L;
        for (int i = 0 ; i < 6 ; i ++) {
            LOGGER.debug("result = " + (1 << i));
        }
    }
}
