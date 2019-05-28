package com.huazie.ffs.permission;

/**
 * <p> 定义文件处理的操作类型 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public enum OperateType {

    UPLOAD("UPLOAD", "上传"),
    DOWNLOAD("DOWNLOAD", "下载");

    private String type;

    private String name;

    OperateType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
