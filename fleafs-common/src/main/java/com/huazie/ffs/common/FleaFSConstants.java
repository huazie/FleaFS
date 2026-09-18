package com.huazie.ffs.common;

/**
 * FleaFS 常量类
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.1.0
 */
public final class FleaFSConstants {

    private FleaFSConstants() {
    }

    public static final class FileCategoryConstants {
        /**
         * 最大文件容量
         */
        public static final Long MAX_FILE_SIZE = 100L;
        /**
         * 无需加密【AES、DES 等加密方式请参见框架枚举 EncryptionAlgorithmEnum】
         */
        public static final String NO_NEED_ENCRYPTION = "NONE";
        /**
         * 操作状态【1：启用】
         */
        public static final char OPERATION_STATE_ENABLED = '1';
        /**
         * 授权校验方式I18N键
         */
        public static final String FLEAFS_AUTH_CHECK_MODE = "FLEAFS-AUTH-CHECK-MODE";
    }

    public static final class FileVersionConstants {
        /**
         * 版本编码前缀
         */
        public static final String VERSION_CODE_PREFIX = "V";

        private FileVersionConstants() {
        }
    }

    public static final class DeleteConstants {
        /**
         * 逻辑删除文件保留天数【超过该期限可执行物理删除】
         */
        public static final int LOGIC_DELETE_KEEP_DAYS = 180;

        private DeleteConstants() {
        }
    }

    public static final class AttrConstants {
        /**
         * 授权校验方式
         */
        public static final String ATTR_CODE_AUTH_CHECK_MODE = "AUTH_CHECK_MODE";
        /**
         * 包含系统用户
         */
        public static final String ATTR_CODE_INCLUDE_SYSTEM_USER = "INCLUDE_SYSTEM_USER";
        /**
         * 包含操作用户
         */
        public static final String ATTR_CODE_INCLUDE_OPERATION_USER = "INCLUDE_OPERATION_USER";
        /**
         * 包含用户组
         */
        public static final String ATTR_CODE_INCLUDE_USER_GROUP = "INCLUDE_USER_GROUP";
        /**
         * 排除系统用户
         */
        public static final String ATTR_CODE_EXCLUDE_SYSTEM_USER = "EXCLUDE_SYSTEM_USER";
        /**
         * 排除操作用户
         */
        public static final String ATTR_CODE_EXCLUDE_OPERATION_USER = "EXCLUDE_OPERATION_USER";
        /**
         * 排除用户组
         */
        public static final String ATTR_CODE_EXCLUDE_USER_GROUP = "EXCLUDE_USER_GROUP";
        /**
         * 文件关联的类目编号
         */
        public static final String ATTR_CODE_CATEGORY_ID = "CATEGORY_ID";
    }

    /**
     * 文件管理授权校验常量
     * <p> 授权校验方式按位组合，对应文件类目属性【AUTH_CHECK_MODE】的属性值。
     *
     * @since 1.1.0
     */
    public static final class AuthConstants {
        /**
         * 授权校验方式：无需校验
         */
        public static final int AUTH_CHECK_MODE_NONE = 0;
        /**
         * 授权校验方式位：系统用户授权校验
         */
        public static final int AUTH_CHECK_MODE_SYSTEM_USER = 1;
        /**
         * 授权校验方式位：操作用户授权校验
         */
        public static final int AUTH_CHECK_MODE_OPERATION_USER = 1 << 1;

        private AuthConstants() {
        }
    }

    /**
     * Flea配置数据常量
     *
     * @since 1.0.0
     */
    public static final class ConfigDataConstants {
        /**
         * TOKEN失效配置
         */
        public static final String CONFIG_TYPE_TOKEN_EXPIRY_CONFIG = "TOKEN_EXPIRY_CONFIG";
        /**
         * TOKEN默认失效分钟数【未配置时生效】
         */
        public static final int DEFAULT_TOKEN_EXPIRY_MINUTES = 5;

        private ConfigDataConstants() {
        }
    }

    /**
     * 文件搜索常量
     *
     * @since 1.0.0
     */
    public static final class SearchConstants {
        /**
         * 文件信息索引名
         */
        public static final String FILE_INDEX_NAME = "fleafs_file_info";
        /**
         * 文档字段：文件编号
         */
        public static final String FIELD_FILE_ID = "fileId";
        /**
         * 文档字段：文件名称
         */
        public static final String FIELD_FILE_NAME = "fileName";
        /**
         * 文档字段：文件类型
         */
        public static final String FIELD_FILE_TYPE = "fileType";
        /**
         * 文档字段：文件大小【字节】
         */
        public static final String FIELD_FILE_SIZE = "fileSize";
        /**
         * 文档字段：文件大小描述
         */
        public static final String FIELD_FILE_SIZE_DESC = "fileSizeDesc";
        /**
         * 文档字段：文件状态
         */
        public static final String FIELD_FILE_STATE = "fileState";
        /**
         * 文档字段：创建日期【毫秒时间戳】
         */
        public static final String FIELD_CREATE_DATE = "createDate";
        /**
         * 文档字段 keyword 子字段后缀【用于精确匹配与模糊匹配】
         */
        public static final String KEYWORD_SUFFIX = ".keyword";

        private SearchConstants() {
        }
    }

    /**
     * ElasticSearch 集群常量
     *
     * @since 1.0.0
     */
    public static final class ESConstants {
        /**
         * FleaFS业务配置项集key【详见 flea-config.xml】
         */
        public static final String CONFIG_ITEMS_KEY = "flea-fs-config";
        /**
         * ElasticSearch集群地址配置项key
         */
        public static final String CONFIG_ITEM_ES_HOSTS = "es_hosts";
        /**
         * 默认集群主机
         */
        public static final String DEFAULT_HOST = "127.0.0.1";
        /**
         * 默认集群端口
         */
        public static final int DEFAULT_PORT = 9200;
        /**
         * 集群访问协议
         */
        public static final String PROTOCOL = "http";

        private ESConstants() {
        }
    }

    /**
     * 系统常量
     *
     * @since 1.0.0
     */
    public static final class SystemConstants {
        /**
         * 系统临时目录属性名
         */
        public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

        private SystemConstants() {
        }
    }

    /**
     * IO 常量
     *
     * @since 1.0.0
     */
    public static final class IOConstants {
        /**
         * 流拷贝缓冲区大小【单位：字节】
         */
        public static final int BUFFER_SIZE = 4096;

        private IOConstants() {
        }
    }
}
