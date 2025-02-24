package com.huazie.ffs.base.entity;

import com.huazie.fleaframework.common.FleaEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
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

}