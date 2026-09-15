package com.huazie.ffs.module.search.service.impl;

import com.huazie.ffs.base.entity.FleaFileInfo;
import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.ffs.module.search.service.interfaces.IFleaFileIndexSV;
import com.huazie.ffs.module.search.util.FleaESClientHolder;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Flea文件ES索引服务实现类
 * <p> 基于ElasticSearch RestHighLevelClient维护文件索引文档；
 * 索引操作全部容错处理（仅记录日志，不抛出异常），保证ES不可用时不阻断文件上传、更新、删除主链路。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class FleaFileIndexSVImpl implements IFleaFileIndexSV {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFileIndexSVImpl.class);

    @Override
    public void indexFleaFileInfo(FleaFileInfo fileInfo) {
        if (ObjectUtils.isEmpty(fileInfo) || StringUtils.isBlank(fileInfo.getFileId())) {
            return;
        }

        Object obj = new Object() {};
        try {
            Map<String, Object> doc = new LinkedHashMap<>();
            doc.put(FleaFSConstants.SearchConstants.FIELD_FILE_ID, fileInfo.getFileId());
            doc.put(FleaFSConstants.SearchConstants.FIELD_FILE_NAME, fileInfo.getFileName());
            doc.put(FleaFSConstants.SearchConstants.FIELD_FILE_TYPE, fileInfo.getFileType());
            doc.put(FleaFSConstants.SearchConstants.FIELD_FILE_SIZE, fileInfo.getFileSize());
            doc.put(FleaFSConstants.SearchConstants.FIELD_FILE_SIZE_DESC, fileInfo.getFileSizeDesc());
            doc.put(FleaFSConstants.SearchConstants.FIELD_FILE_STATE, fileInfo.getFileState());
            // 创建日期存毫秒时间戳，保证ES动态映射为数值型date，支持排序
            doc.put(FleaFSConstants.SearchConstants.FIELD_CREATE_DATE,
                    ObjectUtils.isEmpty(fileInfo.getCreateDate()) ? 0L : fileInfo.getCreateDate().getTime());

            IndexRequest request = new IndexRequest(FleaFSConstants.SearchConstants.FILE_INDEX_NAME)
                    .id(fileInfo.getFileId()).source(doc, XContentType.JSON);
            RestHighLevelClient client = FleaESClientHolder.getClient();
            client.index(request, RequestOptions.DEFAULT);

            LOGGER.debug1(obj, "文件ES索引更新成功，fileId = {}", fileInfo.getFileId());
        } catch (Exception e) {
            // 容错处理：ES不可用不阻断文件主链路
            LOGGER.error1(obj, "文件ES索引更新失败，fileId = {}", fileInfo.getFileId(), e);
        }
    }

    @Override
    public void removeFleaFileInfo(String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return;
        }

        Object obj = new Object() {};
        try {
            DeleteRequest request = new DeleteRequest(FleaFSConstants.SearchConstants.FILE_INDEX_NAME, fileId);
            RestHighLevelClient client = FleaESClientHolder.getClient();
            client.delete(request, RequestOptions.DEFAULT);

            LOGGER.debug1(obj, "文件ES索引删除成功，fileId = {}", fileId);
        } catch (Exception e) {
            // 容错处理：ES不可用不阻断文件主链路
            LOGGER.error1(obj, "文件ES索引删除失败，fileId = {}", fileId, e);
        }
    }
}
