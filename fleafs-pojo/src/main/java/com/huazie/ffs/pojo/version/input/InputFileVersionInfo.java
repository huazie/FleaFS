package com.huazie.ffs.pojo.version.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 版本查询业务入参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class InputFileVersionInfo implements Serializable {

    private static final long serialVersionUID = 7523017424080356969L;

    private String fileId; // 文件编号

}
