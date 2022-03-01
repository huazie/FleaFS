package com.huazie.ffs.pojo.download.input;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 文件下载业务入参
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class InputFileDownloadInfo implements Serializable {

    private static final long serialVersionUID = 8724133691307180834L;

    private String fileId; // 文件编号（非鉴权下载时用到）

    private String token; // 下载鉴权令牌

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

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
