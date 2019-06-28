package com.huazie.ffs.web;

import com.huazie.frame.jersey.server.resource.FleaJerseyTestGetResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;

/**
 * <p>  </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Path("resource")
public class Resource extends FleaJerseyTestGetResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(Resource.class);

    @Override
    protected String doGet(String resId) {
        return "This is " + resId + " !!!";
    }

}
