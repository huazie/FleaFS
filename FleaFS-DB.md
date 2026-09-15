
# FleaFS 分库分表

## FleaFS 相关库
|  库名          |  中文描述     |  建表sql    | 
|--------------- |---------------| ---------------| 
|  fleafs        |  FleaFS主库   | 详见[fleafs.sql](sql/fleafs.sql)|
|  fleafs1       |  FleaFS分库1  | 详见[fleafs1.sql](sql/fleafs1.sql)|
|  fleafs2       |  FleaFS分库2  | 详见[fleafs2.sql](sql/fleafs2.sql)|
|  fleafs3       |  FleaFS分库3  | 详见[fleafs3.sql](sql/fleafs3.sql)|
|  fleafs4       |  FleaFS分库4  | 详见[fleafs4.sql](sql/fleafs4.sql)|
|  fleafsconfig  |  FleaFS配置库（Flea框架配置库）  | 建表详见 flea-framework 的 flea-core/fleaconfig.sql；FleaFS业务配置数据详见[fleafsconfig.sql](sql/fleafsconfig.sql)|

## FleaFS 相关表
|  表名（NN: 2位16进制数）  |  中文描述                             |
|-------------------------- |---------------------------------------|  
|  flea_file_info_NN        |  flea文件信息表                        |
|  flea_file_attr_NN        |  flea文件属性表                        |
|  flea_file_version_NN     |  flea文件版本表（记录文件历史版本信息） |
|  flea_token_info_NN   	|  flea鉴权信息表                        |
|  flea_file_category       |  flea文件类目表（与文件授权访问相关）   |
|  flea_category_attr       |  flea类目属性表                        |

## FleaFS 配置数据（fleafsconfig 库）
|  表名                              |  中文描述                       |  FleaFS 配置数据                                       |
|----------------------------------- |--------------------------------|--------------------------------------------------------|
|  flea_config_data                  |  Flea配置数据表                  |  `TOKEN_EXPIRY_CONFIG`（TOKEN失效分钟数）                 |
|  flea_jersey_resource              |  Flea Jersey资源定义表            |  upload、download、update、delete、version、search        |
|  flea_jersey_res_service           |  Flea Jersey资源服务定义表        |  各资源的鉴权与业务服务（共10个）                          |
|  flea_jersey_res_client            |  Flea Jersey资源客户端定义表      |  对应服务的客户端调用定义（共10个）                        |

## FleaFS 应用配置（flea-config.xml）
|  配置项集            |  配置项      |  中文描述                    |
|--------------------- |-------------|-----------------------------|
|  flea-fs-config      |  es_hosts    |  ElasticSearch集群地址       |

> ElaticSearch集群地址支持多节点，格式为 `host:port`，多个节点以逗号分隔，如 `127.0.0.1:9200,127.0.0.2:9200`；
> 未配置时默认使用 `127.0.0.1:9200`。