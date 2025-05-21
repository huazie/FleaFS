package com.huazie.ffs;

import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.ffs.common.util.FileUtils;
import com.huazie.fleaframework.cache.AbstractFleaCache;
import com.huazie.fleaframework.cache.AbstractFleaCacheManager;
import com.huazie.fleaframework.cache.common.CacheEnum;
import com.huazie.fleaframework.cache.common.FleaCacheManagerFactory;
import com.huazie.fleaframework.cache.memcached.config.MemCachedConfig;
import com.huazie.fleaframework.jersey.common.filter.config.FleaJerseyFilterConfig;
import org.junit.Before;
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

    @Before
    public void init() {
        // 设置 Jersey 过滤器链配置文件 路径
        FleaJerseyFilterConfig.setFilePath("flea/jersey/fleafs-jersey-filter.xml");
    }

    @Test
    public void testMemcachedConfig() {
        MemCachedConfig config = MemCachedConfig.getConfig();
        LOGGER.debug("config = {}", config);
    }

    @Test
    public void testCoreFleaCache() {
        AbstractFleaCacheManager manager = FleaCacheManagerFactory.getFleaCacheManager(CacheEnum.FleaCore.getName());
        AbstractFleaCache cache = manager.getCache("fleajerseyresservice");
        LOGGER.debug("Cache={}", cache);
        cache.delete("FLEA_SERVICE_UPLOAD_AUTH_upload");
        cache.getCacheKey();
        LOGGER.debug(cache.getCacheName() + ">>>" + cache.getCacheDesc());
    }

    @Test
    public void testJerseyFilterConfig() {
        LOGGER.debug("before={}", FleaJerseyFilterConfig.getBeforeFilters());
        LOGGER.debug("service={}", FleaJerseyFilterConfig.getServiceFilters());
        LOGGER.debug("after={}", FleaJerseyFilterConfig.getAfterFilters());
        LOGGER.debug("error={}", FleaJerseyFilterConfig.getErrorFilters());
        LOGGER.debug("i18n-error-mapping={}", FleaJerseyFilterConfig.getI18nErrorMapping("ERROR-JERSEY-FILTER0000000003"));
    }

    @Test
    public void test() {
        OperateTypeEnum value = OperateTypeEnum.values()[5];
        LOGGER.debug("value={}", value);
        LOGGER.debug(FileUtils.getFileExtension("test.txt"));
    }
}
