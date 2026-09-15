package com.huazie.ffs.pojo.update.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 文件更新业务入参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class InputFileUpdateInfo implements Serializable {

    private static final long serialVersionUID = 2994813954505164951L;

    private String token; // 更新鉴权令牌

}
