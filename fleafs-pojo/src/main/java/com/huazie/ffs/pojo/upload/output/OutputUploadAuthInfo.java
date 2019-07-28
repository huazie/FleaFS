package com.huazie.ffs.pojo.upload.output;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <p> 上传鉴权业务出参定义 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class OutputUploadAuthInfo implements Serializable {

    private static final long serialVersionUID = -8413595899070514907L;

    private String token; // 上传鉴权令牌

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
