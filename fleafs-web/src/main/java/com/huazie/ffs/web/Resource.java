package com.huazie.ffs.web;

import com.huazie.frame.jersey.api.data.FleaJerseyRequestData;
import com.huazie.frame.jersey.api.data.FleaJerseyResponseData;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public String getResource(@QueryParam("id") final String resId) {
        return "This is " + resId + " !!!";
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public FleaJerseyResponseData upload(FleaJerseyRequestData requestData) {

        return null;
    }

}
