package com.huazie.ffs.pojo.download.output;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 下载鉴权业务出参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class OutputDownloadAuthInfo implements Serializable {

    private static final long serialVersionUID = 5689920399219551237L;

    private String token; // 下载鉴权令牌

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
