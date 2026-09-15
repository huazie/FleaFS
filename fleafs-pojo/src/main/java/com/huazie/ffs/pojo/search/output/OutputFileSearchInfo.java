package com.huazie.ffs.pojo.search.output;

import java.io.Serializable;
import java.util.List;

/**
 * 文件搜索业务出参
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class OutputFileSearchInfo implements Serializable {

    private static final long serialVersionUID = 8862704336153108227L;

    /**
     * 符合条件的总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 当前页文件列表
     */
    private List<OutputFileSearchItem> files;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<OutputFileSearchItem> getFiles() {
        return files;
    }

    public void setFiles(List<OutputFileSearchItem> files) {
        this.files = files;
    }
}
