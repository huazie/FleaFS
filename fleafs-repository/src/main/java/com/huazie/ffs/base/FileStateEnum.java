package com.huazie.ffs.base;

/**
 * 文件状态枚举
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public enum FileStateEnum {

    FILE_PENDING_UPLOAD(0, "待上传"),
    FILE_PENDING_REVIEW(1, "待审核"),
    FILE_IN_USE(2, "使用中"),
    FILE_REVIEW_REJECTED(3, "审核不通过"),
    FILE_LOGIC_DELETED(4, "逻辑删除");

    private int state;

    private String desc;

    FileStateEnum(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }
}
