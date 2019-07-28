package com.huazie.ffs;

import javax.ws.rs.ApplicationPath;

import com.huazie.frame.jersey.server.core.FleaResourceConfig;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * <p> FleaFS 资源入口 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@ApplicationPath("/fleafs/*")
public class FleaFSResourceConfig extends FleaResourceConfig {

    /**
     * <p> 无参构造方法 </p>
     *
     * @since 1.0.0
     */
    public FleaFSResourceConfig() {
        super();
    }
}