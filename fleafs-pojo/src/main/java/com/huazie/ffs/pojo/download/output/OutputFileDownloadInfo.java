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

    private static final long serialVersionUID = -7771699230002428344L;

    private String fileName;

    private String fileInput;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileInput() {
        return fileInput;
    }

    public void setFileInput(String fileInput) {
        this.fileInput = fileInput;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
