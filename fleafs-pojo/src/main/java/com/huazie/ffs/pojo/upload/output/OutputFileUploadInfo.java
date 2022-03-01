package com.huazie.ffs.pojo.upload.output;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 文件上传出参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class OutputFileUploadInfo implements Serializable {

    private static final long serialVersionUID = -4807723256322765142L;

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
