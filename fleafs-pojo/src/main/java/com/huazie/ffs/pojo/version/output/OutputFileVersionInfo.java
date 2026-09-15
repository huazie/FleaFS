package com.huazie.ffs.pojo.version.output;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 版本查询业务出参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OutputFileVersionInfo implements Serializable {

    private static final long serialVersionUID = -2243077064906428305L;

    private String fileId; // 文件编号

    private Integer versionCount; // 版本总数

    private List<OutputFileVersionItem> versionList; // 版本明细列表（按版本编号倒序）

}
