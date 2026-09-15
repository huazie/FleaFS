package com.huazie.ffs.pojo.update.output;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 更新鉴权业务出参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OutputUpdateAuthInfo implements Serializable {

    private static final long serialVersionUID = -4372176100318787766L;

    private String token; // 更新鉴权令牌

}
