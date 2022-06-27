package com.huazie.ffs;

import com.huazie.fleaframework.cache.AbstractFleaCache;
import com.huazie.fleaframework.cache.AbstractFleaCacheManager;
import com.huazie.fleaframework.cache.common.CacheEnum;
import com.huazie.fleaframework.cache.common.FleaCacheManagerFactory;
import com.huazie.fleaframework.cache.memcached.config.MemCachedConfig;
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

    @Test
    public void testCoreFleaCache() {
        try {
            AbstractFleaCacheManager manager = FleaCacheManagerFactory.getFleaCacheManager(CacheEnum.FleaCore.getName());
            AbstractFleaCache cache = manager.getCache("fleajerseyi18nerrormapping");
            LOGGER.debug("Cache={}", cache);
            cache.get("jersey-filter-resource_jersey-filter-service_ERROR-AUTH-COMMON0000000008");
            // cache.delete("jersey-filter-resource_jersey-filter-service_ERROR-AUTH-COMMON0000000008");
            cache.getCacheKey();
            LOGGER.debug(cache.getCacheName() + ">>>" + cache.getCacheDesc());
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
        }
    }
}
