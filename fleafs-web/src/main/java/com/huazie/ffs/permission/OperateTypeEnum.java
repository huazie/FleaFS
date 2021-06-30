package com.huazie.ffs.permission;

/**
 * 操作类型枚举，定义了文件管理的各个操作类型
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public enum OperateTypeEnum {

    UPLOAD("UPLOAD", "上传"),
    DOWNLOAD("DOWNLOAD", "下载"),
    UPDATE("UPDATE", "更新"),
    DELETE("DELETE", "删除"),
    SEARCH("SEARCH", "搜索"),
    VERSION("VERSION", "查看文件版本");

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
