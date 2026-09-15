package com.huazie.ffs.pojo.update.output;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 文件更新业务出参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OutputFileUpdateInfo implements Serializable {

    private static final long serialVersionUID = 5033179675337201862L;

    private String fileId; // 文件编号

    private String versionCode; // 版本编码

}
