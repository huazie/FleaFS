package com.huazie.ffs.pojo.download.output;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <p> 文件下载业务出参 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class OutputFileDownloadInfo implements Serializable {

    private String acctId; // 文件上传账户

    private String systemAcctId; // 文件上传系统账户

    private String count; // 文件下载次数

    private String uploadDate; // 文件上传日期

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getSystemAcctId() {
        return systemAcctId;
    }

    public void setSystemAcctId(String systemAcctId) {
        this.systemAcctId = systemAcctId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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
