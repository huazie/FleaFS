package com.huazie.ffs.pojo.search.output;

import java.io.Serializable;

/**
 * 文件搜索单条结果出参
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class OutputFileSearchItem implements Serializable {

    private static final long serialVersionUID = 5168932049187229127L;

    /**
     * 文件编号
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小【单位：B】
     */
    private Long fileSize;

    /**
     * 文件大小描述
     */
    private String fileSizeDesc;

    /**
     * 文件状态
     */
    private Integer fileState;

    /**
     * 创建日期【格式：yyyy-MM-dd HH:mm:ss】
     */
    private String createDate;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSizeDesc() {
        return fileSizeDesc;
    }

    public void setFileSizeDesc(String fileSizeDesc) {
        this.fileSizeDesc = fileSizeDesc;
    }

    public Integer getFileState() {
        return fileState;
    }

    public void setFileState(Integer fileState) {
        this.fileState = fileState;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
