package com.huazie.ffs;

import com.huazie.frame.cache.memcached.config.MemCachedConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p></p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFSTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleaFSTest.class);

    @Test
    public void testMemcachedConfig() {
        MemCachedConfig config = MemCachedConfig.getConfig();
        LOGGER.debug("config = {}", config);
    }

}
