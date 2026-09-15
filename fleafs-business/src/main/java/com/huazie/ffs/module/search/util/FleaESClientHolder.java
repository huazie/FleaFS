package com.huazie.ffs.module.search.util;

import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.ffs.util.FleaFSUtils;
import com.huazie.fleaframework.common.CommonConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * FleaFS ElasticSearch客户端持有器【双重检查锁单例懒加载】
 * <p> 集群地址来自配置文件 flea-config.xml 【flea-fs-config / es_hosts】，未配置时默认 127.0.0.1:9200
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaESClientHolder {

    private static volatile RestHighLevelClient client;

    private FleaESClientHolder() {
    }

    /**
     * 获取ElasticSearch高级别REST客户端
     *
     * @return RestHighLevelClient实例
     * @throws IllegalStateException 客户端初始化失败时抛出
     */
    public static RestHighLevelClient getClient() {
        if (client == null) {
            synchronized (FleaESClientHolder.class) {
                if (client == null) {
                    String esHosts = FleaFSUtils.getEsHosts();
                    client = new RestHighLevelClient(RestClient.builder(parseHosts(esHosts)));
                }
            }
        }
        return client;
    }

    /**
     * 解析ES集群地址字符串为HttpHost数组
     *
     * @param esHosts 集群地址字符串【格式：host:port，多节点逗号分隔】
     * @return HttpHost数组
     */
    private static HttpHost[] parseHosts(String esHosts) {
        String[] hostArr = esHosts.split(CommonConstants.SymbolConstants.COMMA);
        HttpHost[] httpHosts = new HttpHost[hostArr.length];
        for (int i = 0; i < hostArr.length; i++) {
            String hp = hostArr[i].trim();
            if (!hp.contains(CommonConstants.SymbolConstants.COLON)) {
                hp = hp + CommonConstants.SymbolConstants.COLON + FleaFSConstants.ESConstants.DEFAULT_PORT;
            }
            int idx = hp.lastIndexOf(CommonConstants.SymbolConstants.COLON);
            httpHosts[i] = new HttpHost(hp.substring(0, idx), Integer.parseInt(hp.substring(idx + 1)),
                    FleaFSConstants.ESConstants.PROTOCOL);
        }
        return httpHosts;
    }
}
