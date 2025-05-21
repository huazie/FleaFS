package com.huazie.ffs.base.entity;

import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.FleaEntity;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.db.common.FleaTable;
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

import static com.huazie.ffs.common.FleaFSConstants.FileCategoryConstants.MAX_FILE_SIZE;
import static com.huazie.ffs.common.FleaFSConstants.FileCategoryConstants.NO_NEED_ENCRYPTION;

/**
 * Flea文件类目表对应的实体类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "flea_file_category")
@FleaTable(splitLibFlag = false)
public class FleaFileCategory extends FleaEntity {

    private static final long serialVersionUID = 7947199149577679059L;

    @Id
    @Column(name = "category_id", unique = true, nullable = false)
    private Long categoryId; // 类目编号

    @Column(name = "category_code", nullable = false)
    private String categoryCode; // 类目编码

    @Column(name = "category_name")
    private String categoryName; // 类目名称

    @Column(name = "parent_id", nullable = false)
    private Long parentId; // 父类目编号

    @Column(name = "encrypt_type", nullable = false)
    private String encryptType; // 文件加密方式【AES、DES、NONE(无需加密)】

    @Column(name = "max_file_size", nullable = false)
    private Long maxFileSize; // 最大文件容量【单位：MB】

    @Column(name = "operation_state")
    private String operationState; // 操作状态【每一位代表一个文件管理操作的启用状态（0：关闭 1：启用）】

    @Column(name = "state", nullable = false)
    private Integer state; // 类目状态(0: 删除 1: 正常）

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate; // 创建日期

    @Column(name = "done_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneDate; // 修改日期

    @Column(name = "remarks")
    private String remarks; // 备注信息

    public FleaFileCategory() {
    }

    public FleaFileCategory(Long categoryId, String categoryCode, String categoryName, String operationState, String remarks) {
        this(categoryId, categoryCode, categoryName, null, NO_NEED_ENCRYPTION, MAX_FILE_SIZE, operationState, remarks);
    }

    public FleaFileCategory(Long categoryId, String categoryCode, String categoryName, Long parentId, String encryptType, Long maxFileSize, String operationState, String remarks) {
        this.categoryId = categoryId;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        if (ObjectUtils.isEmpty(parentId)) {
            parentId = CommonConstants.NumeralConstants.MINUS_ONE;
        }
        this.parentId = parentId;
        this.encryptType = encryptType;
        this.maxFileSize = maxFileSize;
        this.operationState = operationState;
        this.state = EntityStateEnum.IN_USE.getState();
        this.createDate = DateUtils.getCurrentTime();
        this.remarks = remarks;
    }
}