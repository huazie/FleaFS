package com.huazie.ffs.pojo.download.input;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 下载鉴权业务入参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class InputDownloadAuthInfo implements Serializable {

    private static final long serialVersionUID = 6849188299874561970L;

    private String fileId; // 文件编号

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
