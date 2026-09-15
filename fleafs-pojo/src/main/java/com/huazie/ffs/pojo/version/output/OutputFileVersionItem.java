package com.huazie.ffs.pojo.version.output;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 文件版本明细业务出参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OutputFileVersionItem implements Serializable {

    private static final long serialVersionUID = 1872065128790449481L;

    private Long versionId; // 版本编号

    private String versionCode; // 版本编码

    private String versionName; // 版本名称

    private String versionDesc; // 版本描述

    private String fileName; // 文件名称

    private String fileType; // 文件类型

    private Long fileSize; // 文件大小【单位：B】

    private String fileSizeDesc; // 文件大小描述

    private String createDate; // 版本生成日期

}
