package com.huazie.ffs.module.search.service.impl;

import com.huazie.ffs.base.FileStateEnum;
import com.huazie.ffs.base.util.FleaFSCheck;
import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.ffs.module.search.service.interfaces.IFleaSearchSV;
import com.huazie.ffs.module.search.util.FleaESClientHolder;
import com.huazie.ffs.pojo.search.input.InputFileSearchInfo;
import com.huazie.ffs.pojo.search.output.OutputFileSearchInfo;
import com.huazie.ffs.pojo.search.output.OutputFileSearchItem;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.DateFormatEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.util.DateUtils;
import com.huazie.fleaframework.common.util.ExceptionUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.ffs.common.exceptions.FleaFSException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Flea搜索服务实现类，基于ElasticSearch实现文件搜索
 * <p> 检索范围：状态为使用中的文件；支持文件名称关键词模糊匹配、文件类型精确过滤，按创建日期倒序分页返回
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaSearchSVImpl implements IFleaSearchSV {

    @Override
    public OutputFileSearchInfo fileSearch(InputFileSearchInfo input) throws CommonException {
        FleaFSCheck.checkEmpty(input, "文件搜索业务入参");

        int pageNum = input.getPageNum();
        int pageSize = input.getPageSize();

        try {
            RestHighLevelClient client = FleaESClientHolder.getClient();

            // 构造查询条件：仅检索使用中的文件
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.termQuery(FleaFSConstants.SearchConstants.FIELD_FILE_STATE,
                            FileStateEnum.FILE_IN_USE.getState()));

            // 文件名称关键词模糊匹配
            String fileName = input.getFileName();
            if (StringUtils.isNotBlank(fileName)) {
                boolQuery.must(QueryBuilders.wildcardQuery(
                        FleaFSConstants.SearchConstants.FIELD_FILE_NAME + FleaFSConstants.SearchConstants.KEYWORD_SUFFIX,
                        CommonConstants.SymbolConstants.ASTERISK + fileName.trim().toLowerCase()
                                + CommonConstants.SymbolConstants.ASTERISK));
            }

            // 文件类型精确过滤
            String fileType = input.getFileType();
            if (StringUtils.isNotBlank(fileType)) {
                boolQuery.filter(QueryBuilders.termQuery(
                        FleaFSConstants.SearchConstants.FIELD_FILE_TYPE + FleaFSConstants.SearchConstants.KEYWORD_SUFFIX,
                        fileType.trim()));
            }

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                    .query(boolQuery)
                    .from((pageNum - 1) * pageSize)
                    .size(pageSize)
                    .sort(FleaFSConstants.SearchConstants.FIELD_CREATE_DATE, SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest(FleaFSConstants.SearchConstants.FILE_INDEX_NAME)
                    .source(sourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 解析搜索结果
            OutputFileSearchInfo output = new OutputFileSearchInfo();
            output.setTotal(searchResponse.getHits().getTotalHits() == null ? 0L : searchResponse.getHits().getTotalHits().value);
            output.setPageNum(pageNum);
            output.setPageSize(pageSize);
            output.setFiles(parseHits(searchResponse.getHits().getHits()));
            return output;
        } catch (CommonException e) {
            throw e;
        } catch (Exception e) {
            ExceptionUtils.throwFleaException(FleaFSException.class, "文件搜索失败，请检查ElasticSearch服务是否可用", e);
            return null;
        }
    }

    /**
     * 解析ES搜索命中结果为文件搜索列表
     *
     * @param hits ES搜索命中数组
     * @return 文件搜索列表
     */
    private List<OutputFileSearchItem> parseHits(SearchHit[] hits) {
        List<OutputFileSearchItem> files = new ArrayList<>();
        if (hits == null || hits.length == 0) {
            return files;
        }

        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSourceAsMap();
            OutputFileSearchItem item = new OutputFileSearchItem();
            item.setFileId(StringUtils.valueOf(source.get(FleaFSConstants.SearchConstants.FIELD_FILE_ID)));
            item.setFileName(StringUtils.valueOf(source.get(FleaFSConstants.SearchConstants.FIELD_FILE_NAME)));
            item.setFileType(StringUtils.valueOf(source.get(FleaFSConstants.SearchConstants.FIELD_FILE_TYPE)));
            Object fileSize = source.get(FleaFSConstants.SearchConstants.FIELD_FILE_SIZE);
            item.setFileSize(fileSize instanceof Number ? ((Number) fileSize).longValue() : 0L);
            item.setFileSizeDesc(StringUtils.valueOf(source.get(FleaFSConstants.SearchConstants.FIELD_FILE_SIZE_DESC)));
            Object fileState = source.get(FleaFSConstants.SearchConstants.FIELD_FILE_STATE);
            item.setFileState(fileState instanceof Number ? ((Number) fileState).intValue() : null);
            Object createDate = source.get(FleaFSConstants.SearchConstants.FIELD_CREATE_DATE);
            if (createDate instanceof Number) {
                item.setCreateDate(DateUtils.date2String(new Date(((Number) createDate).longValue()),
                        DateFormatEnum.YYYY_MM_DDHH_MM_SS));
            }
            files.add(item);
        }
        return files;
    }
}
