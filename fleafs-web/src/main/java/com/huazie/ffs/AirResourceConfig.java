package com.huazie.ffs;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * <p>  </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@ApplicationPath("/fleafs/*")
public class AirResourceConfig extends ResourceConfig {
	/**
	 * <p> Constructor for AirResourceConfig. </p>
	 */
	public AirResourceConfig() {
		packages("com.huazie.ffs.web");
	}
}