# FleaFS

Flea File Server, built on [Flea Framework](https://github.com/huazie/flea-framework), supports access from various authorized systems and provides distributed file management with unified authorized access.

[中文说明/Chinese Documentation](README.md)

## Features

| Feature | Description |
|---------|-------------|
| File Upload | Upload files with an optional file category and optional file attributes |
| File Download | Download by file ID or token, with optional content encryption/decryption |
| File Update | Update file content and attributes, with historical versions recorded automatically |
| File Delete | Delete a specific version or all versions of a file by file ID |
| File Search | Cross-category file search based on ElasticSearch |
| Version Query | Query the historical version information of a file |
| Category Management | File category management with category-level auth check and operation state control |

## Architecture

1. FastDFS [Distributed File Storage]
2. ElasticSearch [File Indexing and Flexible Search]
3. Flea Framework (v2.0.0)
   > Flea Auth (Unified authorization: system user, operation user and user group checks)

   > Flea Cache (Integrates MemCached and Redis)

   > Flea DB (JPA implementation of split databases and split tables, based on EclipseLink)

   > Flea Jersey (RESTful web services based on Jersey, with auth check filter chain)
4. MySQL [Split databases and split tables for the file server, see [FleaFS-DB.md](FleaFS-DB.md)]
5. Spring [Dependency Injection, JPA Transaction Management, Cache Management]

## Modules

| Module | Description |
|--------|-------------|
| fleafs-common | Common definitions such as constants (`FleaFSConstants`) and operate type enums |
| fleafs-pojo | Request and response POJO definitions for each interface |
| fleafs-repository | Data access layer, entity DAO/SV wrappers, supporting split databases and split tables |
| fleafs-business | Business service implementations: six modules (upload / download / update / delete / version / search) + the auth check module |
| fleafs-config | Framework and application configuration: `flea-config.xml`, Jersey filter chain definitions, i18n resources |
| fleafs-web | Web application entry: Jersey resource interfaces, request filters, packaging and deployment |

## File Management Auth Check

FleaFS performs unified checks on each file management request through the filter chain:

```
DataPreCheckFilter(order=1) → AuthCheckFilter(order=2, Flea Framework) → FleaFSAuthCheckFilter(order=3, FleaFS)
```

`FleaFSAuthCheckFilter` performs the category-level check in three steps:

1. **Resource code validation**: the requested resource code must be registered in `flea_jersey_resource`;
2. **Category existence and operation state check**: check whether the operation is enabled via the corresponding bit of `flea_file_category.operation_state`;
3. **Category-level auth check**: determine whether to check system users, operation users and user groups according to the bit value of the category attribute `AUTH_CHECK_MODE`.

`AUTH_CHECK_MODE` is a bit flag:

| Bit Value | Meaning |
|-----------|---------|
| `0` | No check needed |
| `1` (bit0) | Check system users |
| `2` (bit1) | Check operation users |
| `3` | Check both system users and operation users |

Category attribute codes support a `_<operation index>` (1~6) suffix, so the auth check mode can be configured separately for the six operations (upload / download / update / delete / search / version). The suffixed value takes priority; otherwise it falls back to the common value.

## Database

See [FleaFS-DB.md](FleaFS-DB.md) for the split database/table design. Table creation scripts are located in the [sql](sql/) directory:

- `fleafs`: main database (global tables such as file categories)
- `fleafs1` ~ `fleafs4`: 4 split databases, where `flea_file_info` / `flea_file_attr` / `flea_file_version` / `flea_token_info` are split into tables by a 2-digit hexadecimal sharding number
- `fleafsconfig`: Flea framework configuration database (Jersey resource registration, config data)
- `fleaauth`: Flea authorization database (users, roles, privileges, functions)

## Quick Start

### Requirements

- JDK 1.8+
- Maven 3.x
- MySQL 5.7+
- FastDFS cluster
- ElasticSearch cluster (defaults to `127.0.0.1:9200` if not configured)

### Build

```bash
mvn clean install -Dmaven.test.skip=true
```

### Database Initialization

1. Create the databases: `fleafs`, `fleafs1` ~ `fleafs4`, `fleafsconfig` (utf8 charset);
2. Execute the table creation scripts in the [sql](sql/) directory (`fleafs*.sql` contains `DROP TABLE` statements; re-running them will clear the tables, so be careful);
3. Execute flea-framework's `flea-core/fleaconfig.sql` and [sql/fleafsconfig.sql](sql/fleafsconfig.sql) to initialize the configuration database.

### Application Configuration

The application configuration is centralized in `fleafs-config/src/main/resources/flea/flea-config.xml`:

- `mysql-*` config item sets: database connection information;
- `flea-fs-config` / `es_hosts`: ElasticSearch cluster addresses. Multiple nodes are separated by commas, defaulting to `127.0.0.1:9200` if not configured (restart the application after changing).

> Note: Jersey resources/services/clients are loaded from the `fleafsconfig` database at runtime. New resource interfaces must be registered in the configuration database first.

## LICENSE

[MIT](LICENSE)
