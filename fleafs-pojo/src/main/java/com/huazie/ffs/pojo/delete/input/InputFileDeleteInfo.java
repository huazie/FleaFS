package com.huazie.ffs.pojo.delete.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 文件删除业务入参定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class InputFileDeleteInfo implements Serializable {

    private static final long serialVersionUID = -1293847722450204761L;

    private String token; // 删除鉴权令牌

}
