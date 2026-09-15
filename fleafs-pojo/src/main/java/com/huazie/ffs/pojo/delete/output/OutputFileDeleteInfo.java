package com.huazie.ffs.pojo.delete.output;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 文件删除业务出参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OutputFileDeleteInfo implements Serializable {

    private static final long serialVersionUID = 8241039230881408857L;

    private String fileId; // 文件编号

}
