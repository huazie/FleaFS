package com.huazie.ffs;


import com.huazie.fleaframework.jersey.server.core.FleaResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * FleaFS 资源入口
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@ApplicationPath("/fleafs/*")
public class FleaFSResourceConfig extends FleaResourceConfig {

    /**
     * 无参构造方法
     *
     * @since 1.0.0
     */
    public FleaFSResourceConfig() {
        super();
    }
}