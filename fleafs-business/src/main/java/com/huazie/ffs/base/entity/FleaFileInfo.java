package com.huazie.ffs.base.entity;

import com.huazie.fleaframework.common.FleaEntity;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Flea文件信息表对应的实体类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "flea_file_info")
public class FleaFileInfo extends FleaEntity {

    private static final long serialVersionUID = -2405733820669158569L;

    @Id
    @Column(name = "file_id", unique = true, nullable = false)
    private String fileId; // 文件编号

    @Column(name = "file_name")
    private String fileName; // 文件名称

    @Column(name = "file_path")
    private String filePath; // 文件路径

    @Column(name = "file_type", nullable = false)
    private String fileType; // 文件类型

    @Column(name = "file_size", nullable = false)
    private Long fileSize; // 文件大小【单位：B】

    @Column(name = "file_size_desc")
    private String fileSizeDesc; // 文件大小描述

    @Column(name = "file_version_id", nullable = false)
    private Long fileVersionId; // 文件版本编号

    @Column(name = "file_state", nullable = false)
    private Integer fileState; // 文件状态【0: 待上传 1：待审核 2：使用中 3：审核不通过 4：删除】

    @Column(name = "fastdfs_id")
    private String fastdfsId; // fastdfs文件编号

    @Column(name = "secret_key")
    private String secretKey; // 密钥

    @Column(name = "user_id")
    private Long userId; // 操作用户编号

    @Column(name = "system_id")
    private Long systemId; // 系统用户编号

    @Column(name = "state", nullable = false)
    private Integer state; // 文件记录状态(0: 删除 1: 正常）

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate; // 创建日期

    @Column(name = "done_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneDate; // 修改日期

    @Column(name = "remarks")
    private String remarks; // 备注信息

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public Long getFileVersionId() {
        return fileVersionId;
    }

    public void setFileVersionId(Long fileVersionId) {
        this.fileVersionId = fileVersionId;
    }

    public Integer getFileState() {
        return fileState;
    }

    public void setFileState(Integer fileState) {
        this.fileState = fileState;
    }

    public String getFastdfsId() {
        return fastdfsId;
    }

    public void setFastdfsId(String fastdfsId) {
        this.fastdfsId = fastdfsId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}