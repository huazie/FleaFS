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
         * 无需加密
         */
        public static final String NO_NEED_ENCRYPTION = "NONE";
        /**
         * 授权校验方式I18N键
         */
        public static final String FLEAFS_AUTH_CHECK_MODE = "FLEAFS-AUTH-CHECK-MODE";
    }

    public static final class TokenConstants {


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

    public static final class ConfigDataConstants {
        /**
         * TOKEN失效配置
         */
        public static final String CONFIG_TYPE_TOKEN_EXPIRY_CONFIG = "TOKEN_EXPIRY_CONFIG";
    }
}
