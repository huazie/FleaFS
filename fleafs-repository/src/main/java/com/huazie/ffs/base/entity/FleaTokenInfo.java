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

}