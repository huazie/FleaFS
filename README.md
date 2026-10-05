# FleaFS
[![Flea Framework](https://img.shields.io/badge/flea--framework-2.0.0-green?style=flat)](https://github.com/huazie/flea-framework) [![license](https://img.shields.io/badge/license-MIT-orange)](https://github.com/Huazie/FleaFS/blob/main/LICENSE) [![GitHub Repo stars](https://img.shields.io/github/stars/Huazie/FleaFS?style=flat)](https://github.com/Huazie/FleaFS/stargazers)

Flea文件服务器，基于 [Flea Framework](https://github.com/huazie/flea-framework) 构建，支持各授权系统接入，实现文件的分布式管理与统一授权访问。

[英文说明/English Documentation](README_EN.md)

## 功能介绍

| 功能 | 描述 |
|------|------|
| 文件上传 | 支持文件上传，可指定文件类目，文件属性可选 |
| 文件下载 | 支持按文件编号或令牌下载，内容加解密可选 |
| 文件更新 | 支持更新文件内容与属性，自动记录历史版本 |
| 文件删除 | 支持按文件编号删除指定版本或全部版本 |
| 文件搜索 | 基于 ElasticSearch 的跨类目文件检索 |
| 版本查询 | 查询文件的历史版本信息 |
| 类目管理 | 文件类目管理，支持类目级授权校验与操作状态控制 |

## 应用架构

1. FastDFS【分布式文件存储】
2. ElasticSearch【文件索引与弹性搜索】
3. Flea Framework（v2.0.0）
   > Flea Auth（统一授权，系统用户、操作用户、用户组校验）

   > Flea Cache（整合MemCached和Redis接入）

   > Flea DB（分库分表的JPA实现，基于EclipseLink）

   > Flea Jersey（基于Jersey开发的REST式Web服务，含鉴权过滤器链）
4. MySQL【文件服务器分库分表，详见[FleaFS-DB.md](FleaFS-DB.md)】
5. Spring【依赖注入、JPA事务管理、缓存管理】

## 模块说明

| 模块 | 描述 |
|------|------|
| fleafs-common | 通用常量（`FleaFSConstants`）、操作类型枚举等公共定义 |
| fleafs-pojo | 各接口的请求与响应POJO定义 |
| fleafs-repository | 数据访问层，实体DAO/SV封装，支持分库分表 |
| fleafs-business | 业务服务实现：upload / download / update / delete / version / search 六大模块 + auth授权校验模块 |
| fleafs-config | 框架与应用配置：`flea-config.xml`、Jersey过滤器链定义、i18n国际化资源 |
| fleafs-web | Web应用入口：Jersey资源接口、请求过滤器、打包部署 |

## 文件管理授权校验

FleaFS 通过过滤器链对每次文件管理请求做统一校验：

```
DataPreCheckFilter(order=1) → AuthCheckFilter(order=2，Flea框架) → FleaFSAuthCheckFilter(order=3，FleaFS)
```

`FleaFSAuthCheckFilter` 承担类目级校验，分三步：

1. **资源码合法性校验**：请求资源码必须已在 `flea_jersey_resource` 登记；
2. **类目存在性与操作状态校验**：按 `flea_file_category.operation_state` 对应操作位判断该操作是否启用；
3. **类目级授权校验**：按类目属性 `AUTH_CHECK_MODE` 的位值判断是否需要校验系统用户、操作用户与用户组。

`AUTH_CHECK_MODE` 按位取值：

| 位值 | 含义 |
|------|------|
| `0` | 无需校验 |
| `1`（bit0） | 校验系统用户 |
| `2`（bit1） | 校验操作用户 |
| `3` | 系统用户与操作用户都校验 |

类目属性码支持 `_<操作序号>`（1~6）后缀，可对上传/下载/更新/删除/搜索/版本六种操作分别配置授权方式；带后缀优先，未配置时回退通用值。

## 数据库

分库分表方案详见 [FleaFS-DB.md](FleaFS-DB.md)，相关建表脚本位于 [sql](sql/) 目录：

- `fleafs`：主库（类目等全局表）
- `fleafs1` ~ `fleafs4`：4个分库，`flea_file_info` / `flea_file_attr` / `flea_file_version` / `flea_token_info` 按2位十六进制分片号分表
- `fleafsconfig`：Flea框架配置库（Jersey资源注册、配置数据）
- `fleaauth`：Flea授权库（用户、角色、权限、功能）

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.x
- MySQL 5.7+
- FastDFS 集群
- ElasticSearch 集群（未配置时默认 `127.0.0.1:9200`）

### 构建

```bash
mvn clean install -Dmaven.test.skip=true
```

### 数据库初始化

1. 创建数据库：`fleafs`、`fleafs1` ~ `fleafs4`、`fleafsconfig`（utf8字符集）；
2. 执行 [sql](sql/) 目录下的建表脚本（`fleafs*.sql` 含 `DROP TABLE`，重跑即清表，请谨慎操作）；
3. 执行 flea-framework 的 `flea-core/fleaconfig.sql` 与 [sql/fleafsconfig.sql](sql/fleafsconfig.sql)，完成配置库初始化。

### 应用配置

应用配置集中在 `fleafs-config/src/main/resources/flea/flea-config.xml`：

- `mysql-*` 配置项集：各数据库连接信息；
- `flea-fs-config` / `es_hosts`：ElasticSearch集群地址，多节点以逗号分隔，未配置时默认 `127.0.0.1:9200`（修改后需重启应用）。

> 注意：Jersey资源/服务/客户端在运行期从 `fleafsconfig` 配置库读取，新增资源接口需先在配置库登记。

## LICENSE

[MIT](LICENSE)
