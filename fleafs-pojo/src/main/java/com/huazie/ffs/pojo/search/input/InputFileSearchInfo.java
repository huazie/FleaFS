package com.huazie.ffs.pojo.search.input;

import java.io.Serializable;

/**
 * 文件搜索业务入参
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class InputFileSearchInfo implements Serializable {

    private static final long serialVersionUID = 3393278477264049954L;

    /**
     * 默认页码
     */
    private static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页条数
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 每页最大条数
     */
    private static final int MAX_PAGE_SIZE = 100;

    /**
     * 文件名称关键词【模糊匹配，可为空】
     */
    private String fileName;

    /**
     * 文件类型【如 jpg、pdf，精确匹配，可为空】
     */
    private String fileType;

    /**
     * 当前页码【从1开始，默认1】
     */
    private Integer pageNum;

    /**
     * 每页条数【默认10，最大100】
     */
    private Integer pageSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getPageNum() {
        if (pageNum == null || pageNum < DEFAULT_PAGE_NUM) {
            return DEFAULT_PAGE_NUM;
        }
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
