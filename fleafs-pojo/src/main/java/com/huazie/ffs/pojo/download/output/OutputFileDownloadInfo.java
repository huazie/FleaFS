package com.huazie.ffs.pojo.download.output;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 文件下载业务出参
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class OutputFileDownloadInfo implements Serializable {

    private String uploadAcctId; // 文件上传账户编号

    private String uploadSystemAcctId; // 文件上传系统账户编号

    private String uploadDate; // 文件上传日期

    public String getUploadAcctId() {
        return uploadAcctId;
    }

    public void setUploadAcctId(String uploadAcctId) {
        this.uploadAcctId = uploadAcctId;
    }

    public String getUploadSystemAcctId() {
        return uploadSystemAcctId;
    }

    public void setUploadSystemAcctId(String uploadSystemAcctId) {
        this.uploadSystemAcctId = uploadSystemAcctId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
