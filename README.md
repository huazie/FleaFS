# FleaFS

#### 介绍
Flea文件服务器，支持各系统接入，文件分布式管理。

> 提供如下文件管理功能：
>1. 文件上传
>2. 文件下载
>3. 文件更新
>4. 文件删除
>5. 文件搜索
>6. 版本查询

#### 架构
1. FastDFS【分布式文件存储】
2. Flea FrameWork
   > Flea Auth（统一授权）
   
   > Flea Cache（整合MemCached和Redis接入）
   
   > Flea DB（JPA接入）
   
   > Flea Jersey（基于Jersey开发的REST式的Web服务）
3. MySQL【文件服务器基础表，详见[FleaFS-TABLE.md](./FleaFS-TABLE.md)】
4. Spring【依赖注入、JPA事物管理、缓存管理】

#### 参与贡献
1. huazie
