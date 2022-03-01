package com.huazie.ffs.pojo.upload.input;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 上传鉴权业务入参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class InputUploadAuthInfo implements Serializable {

    private static final long serialVersionUID = -4061044469013410651L;

    private String fileName; // 文件名

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
