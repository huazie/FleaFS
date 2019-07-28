package com.huazie.ffs.pojo.download.input;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <p> 文件下载业务入参 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class InputFileDownloadInfo implements Serializable {

    private static final long serialVersionUID = -4723601945379306946L;
    
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
