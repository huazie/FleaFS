package com.huazie.ffs.pojo.upload.input;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 文件上传入参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class InputFileUploadInfo implements Serializable {

    private static final long serialVersionUID = 8328668851713705342L;

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
