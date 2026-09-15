package com.huazie.ffs.base.entity;

import com.huazie.ffs.base.FileStateEnum;
import com.huazie.ffs.common.util.FileUtils;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.FleaEntity;
import com.huazie.fleaframework.common.FleaSessionManager;
import com.huazie.fleaframework.common.IFleaUser;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Flea文件版本表对应的实体类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "flea_file_version")
public class FleaFileVersion extends FleaEntity {

    private static final long serialVersionUID = -6550279450390266938L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id", unique = true, nullable = false)
    private Long versionId; // 版本编号

    @Column(name = "version_code", nullable = false)
    private String versionCode; // 版本编码

    @Column(name = "version_name", nullable = false)
    private String versionName; // 版本名称

    @Column(name = "version_desc")
    private String versionDesc; // 版本描述

    @Column(name = "file_id", nullable = false)
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

    @Column(name = "file_state", nullable = false)
    private Integer fileState; // 文件状态【0: 待上传 1：待审核 2：使用中 3：审核不通过 4：逻辑删除 5：物理删除】

    @Column(name = "fastdfs_id")
    private String fastdfsId; // fastdfs文件编号

    @Column(name = "secret_key")
    private String secretKey; // 密钥

    @Column(name = "user_id")
    private Long userId; // 操作用户编号

    @Column(name = "system_user_id")
    private Long systemUserId; // 系统用户编号

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

    public FleaFileVersion() {
    }

    /**
     * 构建文件版本记录（用于文件上传、更新时留存文件快照）
     *
     * @param versionCode    版本编码
     * @param versionName    版本名称
     * @param fileId         文件编号
     * @param fileName       文件名称
     * @param filePath       文件路径
     * @param fileType       文件类型
     * @param fileSize       文件大小【单位：B】
     * @param fileSizeDesc   文件大小描述
     * @param fileState      文件状态
     * @param fastdfsId      fastdfs文件编号
     * @param secretKey      密钥【Base64编码】
     * @param remarks        备注信息
     * @since 1.0.0
     */
    public FleaFileVersion(String versionCode, String versionName, String fileId, String fileName, String filePath,
                           String fileType, Long fileSize, String fileSizeDesc, Integer fileState,
                           String fastdfsId, String secretKey, String remarks) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        if (StringUtils.isBlank(fileType))
            fileType = FileUtils.getFileExtension(fileName);
        this.fileType = fileType;
        if (ObjectUtils.isEmpty(fileSize))
            fileSize = CommonConstants.NumeralConstants.ZERO;
        this.fileSize = fileSize;
        this.fileSizeDesc = fileSizeDesc;
        if (ObjectUtils.isEmpty(fileState))
            fileState = FileStateEnum.FILE_PENDING_UPLOAD.getState();
        this.fileState = fileState;
        this.fastdfsId = fastdfsId;
        this.secretKey = secretKey;
        IFleaUser userInfo = FleaSessionManager.getUserInfo();
        if (ObjectUtils.isNotEmpty(userInfo)) {
            this.userId = userInfo.getUserId();
            this.systemUserId = userInfo.getSystemUserId();
        }
        this.state = EntityStateEnum.IN_USE.getState();
        this.createDate = DateUtils.getCurrentTime();
        this.remarks = remarks;
    }

}