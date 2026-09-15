package com.huazie.ffs.module.search.service.interfaces;

import com.huazie.ffs.pojo.search.input.InputFileSearchInfo;
import com.huazie.ffs.pojo.search.output.OutputFileSearchInfo;
import com.huazie.fleaframework.common.exceptions.CommonException;

/**
 * Flea搜索服务接口，提供基于ElasticSearch的文件搜索功能
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaSearchSV {

    /**
     * 文件搜索【按文件名称关键词、文件类型搜索使用中的文件，支持分页】
     *
     * @param input 文件搜索业务入参
     * @return 文件搜索业务出参
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    OutputFileSearchInfo fileSearch(InputFileSearchInfo input) throws CommonException;
}
