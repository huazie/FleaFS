package com.huazie.ffs.pojo.update.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 更新鉴权业务入参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class InputUpdateAuthInfo implements Serializable {

    private static final long serialVersionUID = 6401884311384806914L;

    private String fileId; // 文件编号

}
