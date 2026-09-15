package com.huazie.ffs.pojo.delete.output;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 删除鉴权业务出参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OutputDeleteAuthInfo implements Serializable {

    private static final long serialVersionUID = -5672116978803163994L;

    private String token; // 删除鉴权令牌

}
