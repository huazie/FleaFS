package com.huazie.ffs.module.download.web;

import com.huazie.frame.jersey.common.data.FleaJerseyRequest;
import com.huazie.frame.jersey.common.data.FleaJerseyResponse;
import com.huazie.frame.jersey.server.resource.JerseyPostResource;
import com.huazie.frame.jersey.server.resource.impl.FleaJerseyFGetResource;

import javax.ws.rs.Path;

/**
 * <p> 下载资源类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since .0.0
 */
@Path("download")
public class DownloadResource extends FleaJerseyFGetResource implements JerseyPostResource {

    /**
     * @see JerseyPostResource#doPostResource(FleaJerseyRequest)
     */
    @Override
    public FleaJerseyResponse doPostResource(FleaJerseyRequest request) {
        return doResource(request);
    }

}
