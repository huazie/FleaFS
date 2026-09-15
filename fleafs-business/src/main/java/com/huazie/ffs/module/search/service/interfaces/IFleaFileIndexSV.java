package com.huazie.ffs.module.search.service.interfaces;

import com.huazie.ffs.base.entity.FleaFileInfo;

/**
 * Flea文件ES索引服务接口，提供文件信息在ElasticSearch中的索引维护能力
 * <p> 索引维护采用容错策略：ES不可用时仅记录日志，不阻断文件上传、更新、删除主链路
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileIndexSV {

    /**
     * 新增或更新文件的ES索引文档【以fileId为文档ID，全量覆盖】
     * <p> 仅索引搜索所需字段，fastdfs_id、secret_key 等敏感信息不入索引
     *
     * @param fileInfo Flea文件信息
     * @since 1.0.0
     */
    void indexFleaFileInfo(FleaFileInfo fileInfo);

    /**
     * 删除文件的ES索引文档【用于文件物理删除后清理索引】
     *
     * @param fileId 文件编号
     * @since 1.0.0
     */
    void removeFleaFileInfo(String fileId);
}
