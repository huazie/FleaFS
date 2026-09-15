package com.huazie.ffs.common;

/**
 * 操作类型枚举，定义了文件管理的各个操作类型
 * <p> 其中 {@code index} 表示该操作在文件类目"操作状态"字符串中的位序，
 * 与文件类目表的 operation_state 列按位对应（1：启用 0：关闭）。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public enum OperateTypeEnum {

    UPLOAD(0, "UPLOAD", "文件上传"),
    DOWNLOAD(1, "DOWNLOAD", "文件下载"),
    UPDATE(2, "UPDATE", "文件更新"),
    DELETE(3, "DELETE", "文件删除"),
    SEARCH(4, "SEARCH", "文件搜索"),
    VERSION(5, "VERSION", "版本管理");

    private int index;

    private String type;

    private String name;

    OperateTypeEnum(int index, String type, String name) {
        this.index = index;
        this.type = type;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
