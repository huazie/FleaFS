package com.huazie.ffs.base.entity;

import com.huazie.fleaframework.common.EntityStateEnum;
import com.huazie.fleaframework.common.FleaEntity;
import com.huazie.fleaframework.common.FleaSessionManager;
import com.huazie.fleaframework.common.IFleaUser;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
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
 * Flea鉴权信息表对应的实体类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "flea_token_info")
public class FleaTokenInfo extends FleaEntity {

    private static final long serialVersionUID = -5779012905432932857L;

    @Id
    @Column(name = "token_id", unique = true, nullable = false)
    private String tokenId; // 鉴权令牌

    @Column(name = "file_id", nullable = false)
    private String fileId; // 文件编号

    @Column(name = "category_id", nullable = false)
    private Long categoryId; // 类目编号

    @Column(name = "operation_type", nullable = false)
    private String operationType; // 操作类型

    @Column(name = "state", nullable = false)
    private Integer state; // 文件记录状态(0: 删除 1: 正常）

    @Column(name = "user_id")
    private Long userId; // 操作用户编号

    @Column(name = "system_user_id")
    private Long systemUserId; // 系统用户编号

    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate; // 创建日期

    @Column(name = "done_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneDate; // 修改日期

    @Column(name = "effective_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate; // 生效日期

    @Column(name = "expiry_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate; // 失效日期

    @Column(name = "remarks")
    private String remarks; // 备注信息

    public FleaTokenInfo() {
    }

    public FleaTokenInfo(String tokenId) {
        this.tokenId = tokenId;
    }

    public FleaTokenInfo(String tokenId, String fileId, Long categoryId, String operationType, Date effectiveDate, Date expiryDate, String remarks) {
        this.tokenId = tokenId;
        this.fileId = fileId;
        this.categoryId = categoryId;
        this.operationType = operationType;
        this.state = EntityStateEnum.IN_USE.getState();
        IFleaUser userInfo = FleaSessionManager.getUserInfo();
        if (ObjectUtils.isNotEmpty(userInfo)) {
            this.userId = userInfo.getUserId();
            this.systemUserId = userInfo.getSystemUserId();
        }
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