package com.huazie.ffs.pojo.delete.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 删除鉴权业务入参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class InputDeleteAuthInfo implements Serializable {

    private static final long serialVersionUID = -7084131540081685011L;

    private String fileId; // 文件编号

}
