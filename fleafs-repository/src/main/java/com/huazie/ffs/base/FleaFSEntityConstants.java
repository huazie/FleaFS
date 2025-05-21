package com.huazie.ffs.base;

import com.huazie.fleaframework.common.FleaEntityConstants;

/**
 * FleaFS实体常量
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public final class FleaFSEntityConstants extends FleaEntityConstants {

    private FleaFSEntityConstants() {
    }

    /**
     * 文件类目实体常量
     *
     * @since 1.0.0
     */
    public static final class FileCategoryEntityConstants {
        /**
         * 类目编号
         */
        public static final String E_CATEGORY_ID = "categoryId";
        /**
         * 类目编码
         */
        public static final String E_CATEGORY_CODE = "categoryCode";
        /**
         * 类目名称
         */
        public static final String E_CATEGORY_NAME = "categoryName";
        /**
         * 父类目编号
         */
        public static final String E_PARENT_ID = "parentId";
    }

    /**
     * 文件信息实体常量
     *
     * @since 1.0.0
     */
    public static final class FileInfoEntityConstants {
        /**
         * 文件编号
         */
        public static final String E_FILE_ID = "fileId";
        /**
         * 文件名称
         */
        public static final String E_FILE_NAME = "fileName";
    }

    /**
     * 鉴权信息实体常量
     *
     * @since 1.0.0
     */
    public static final class TokenInfoEntityConstants {
        /**
         * 鉴权令牌
         */
        public static final String E_TOKEN_ID = "tokenId";
    }
}
