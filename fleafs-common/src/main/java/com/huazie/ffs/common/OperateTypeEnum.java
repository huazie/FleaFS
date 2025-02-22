package com.huazie.ffs.common;

/**
 * 操作类型枚举，定义了文件管理的各个操作类型
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public enum OperateTypeEnum {

    UPLOAD("UPLOAD", "文件上传"),
    DOWNLOAD("DOWNLOAD", "文件下载"),
    UPDATE("UPDATE", "文件更新"),
    DELETE("DELETE", "文件删除"),
    SEARCH("SEARCH", "文件搜索"),
    VERSION("VERSION", "版本管理");

    private String type;

    private String name;

    OperateTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
