package com.huazie.ffs.web;

import com.huazie.frame.core.base.cfgdata.bean.FleaConfigDataSpringBean;
import com.huazie.frame.core.base.cfgdata.service.interfaces.IFleaJerseyI18nErrorMappingSV;
import com.huazie.frame.jersey.server.resource.FleaJerseyTestGetResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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

    @Autowired
    protected FleaConfigDataSpringBean fleaConfigDataSpringBean;

    @Override
    protected String doGet(String resId) {
        try {
            fleaConfigDataSpringBean.getMapping("jersey-filter-resource", "jersey-filter-service", "ERROR-JERSEY-FILTER0000000005");
        } catch (Exception e) {
            LOGGER.error("Exception={}", e);
        }
        return "This is " + resId + " !!!";
    }

}
