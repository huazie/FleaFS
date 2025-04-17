
# FleaFS 分库分表

## FleaFS 相关库
|  库名          |  中文描述     |  建表sql    | 
|--------------- |---------------| ---------------| 
|  fleafs        |  FleaFS主库   | 详见[fleafs.sql](sql/fleafs.sql)|
|  fleafs1       |  FleaFS分库1  | 详见[fleafs1.sql](sql/fleafs1.sql)|
|  fleafs2       |  FleaFS分库2  | 详见[fleafs2.sql](sql/fleafs2.sql)|
|  fleafs3       |  FleaFS分库3  | 详见[fleafs3.sql](sql/fleafs3.sql)|
|  fleafs4       |  FleaFS分库4  | 详见[fleafs4.sql](sql/fleafs4.sql)|


## FleaFS 相关表
|  表名（NN: 2位16进制数）  |  中文描述                             |
|-------------------------- |---------------------------------------|  
|  flea_file_info_NN        |  flea文件信息表                        |
|  flea_file_attr_NN        |  flea文件属性表                        |
|  flea_file_version_NN     |  flea文件版本表（记录文件历史版本信息） |
|  flea_token_info_NN   	|  flea鉴权信息表                        |
|  flea_file_category       |  flea文件类目表（与文件授权访问相关）   |
|  flea_category_attr       |  flea类目属性表                        |