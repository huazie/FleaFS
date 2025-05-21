package com.huazie.ffs.base.entity;

import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.FleaEntity;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
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
 * Flea文件属性表对应的实体类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "flea_file_attr")
public class FleaFileAttr extends FleaEntity {

    private static final long serialVersionUID = -3541365493442690740L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attr_id", unique = true, nullable = false)
    private Long attrId; // 属性编号

    @Column(name = "file_id", nullable = false)
    private String fileId; // 文件编号

    @Column(name = "attr_code", nullable = false)
    private String attrCode; // 属性码

    @Column(name = "attr_value")
    private String attrValue; // 属性值

    @Column(name = "attr_desc")
    private String attrDesc; // 属性描述

    @Column(name = "state", nullable = false)
    private Integer state; // 属性状态(0: 删除 1: 正常）

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate; // 创建日期

    @Column(name = "done_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneDate; // 修改日期

    @Column(name = "effective_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate; // 生效日期

    @Column(name = "expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate; // 失效日期

    @Column(name = "remarks")
    private String remarks; // 备注信息

    public FleaFileAttr() {
    }

    public FleaFileAttr(String fileId, String attrCode, String attrValue, String attrDesc, String remarks) {
        this(fileId, attrCode, attrValue, attrDesc, null, null, remarks);
    }

    public FleaFileAttr(String fileId, String attrCode, String attrValue, String attrDesc, Date effectiveDate, Date expiryDate, String remarks) {
        this.fileId = fileId;
        this.attrCode = attrCode;
        this.attrValue = attrValue;
        this.attrDesc = attrDesc;
        this.state = EntityStateEnum.IN_USE.getState();
        this.createDate = DateUtils.getCurrentTime();
        if (ObjectUtils.isEmpty(effectiveDate))
            effectiveDate = DateUtils.getCurrentTime();
        this.effectiveDate = effectiveDate;
        if (ObjectUtils.isEmpty(expiryDate))
            expiryDate = DateUtils.getExpiryTimeForever();
        this.expiryDate = expiryDate;
        this.remarks = remarks;
    }
}