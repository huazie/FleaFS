package com.huazie.ffs;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * <p> REST应用入口类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@ApplicationPath("/fleafs/*")
public class FleaResourceConfig extends ResourceConfig {
    /**
     * <p> 无参构造方法 </p>
     *
     * @since 1.0.0
     */
    public FleaResourceConfig() {
        packages("com.huazie.ffs.web");
    }
}