package com.huazie.ffs.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * <p>  </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Path("resource")
public class Resource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getResource() {
        return "Hello Wrold !!!";
    }
}
