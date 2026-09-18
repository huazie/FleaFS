package com.huazie.ffs.base.util;

import com.huazie.ffs.base.entity.FleaCategoryAttr;
import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.ffs.common.exceptions.FleaFSException;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.i18n.FleaI18nHelper;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.ExceptionUtils;
import com.huazie.fleaframework.common.util.StringUtils;

/**
 * FleaFS 实体工具类
 *
 * <p> 文件类目的授权校验相关属性【{@link FleaFSConstants.AttrConstants}】均支持
 * 按文件管理操作类型单独配置，属性码以【_操作序号】结尾；未配置时，
 * 授权校验取属性码不带操作序号的通用配置。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFSEntityUtils {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFSEntityUtils.class);

    private FleaFSEntityUtils() {
    }

    /**
     * 新建授权校验方式的类目属性
     *
     * @param categoryId    类目编号
     * @param operationType 操作类型
     * @param authCheckMode 授权校验方式
     * @return 授权校验方式的类目属性
     * @since 1.0.0
     */
    public static FleaCategoryAttr newAuthCheckModeAttr(Long categoryId, String operationType, String authCheckMode) {
        String attrCode = newAttrCode(FleaFSConstants.AttrConstants.ATTR_CODE_AUTH_CHECK_MODE, operationType);
        // 【{0}】授权校验方式
        String attrDesc = FleaI18nHelper.i18nForCommon("FLEAFS-CATEGORY00000001", generateValues(operationType));
        // 授权校验方式描述
        String remarks = FleaI18nHelper.i18nForCommon(FleaFSConstants.FileCategoryConstants.FLEAFS_AUTH_CHECK_MODE + authCheckMode);
        return new FleaCategoryAttr(categoryId, attrCode, authCheckMode, attrDesc, remarks);
    }

    /**
     * 新建包含系统用户的类目属性
     *
     * @param categoryId    类目编号
     * @param operationType 操作类型
     * @param systemUsers   系统用户【多个以逗号分隔】
     * @return 包含系统用户的类目属性
     * @since 1.0.0
     */
    public static FleaCategoryAttr newIncludeSystemUsersAttr(Long categoryId, String operationType, String systemUsers) {
        // 【{0}】包含系统用户
        return newUsersAttr(categoryId, operationType,
                FleaFSConstants.AttrConstants.ATTR_CODE_INCLUDE_SYSTEM_USER, "FLEAFS-CATEGORY00000002", systemUsers);
    }

    /**
     * 新建包含操作用户的类目属性
     *
     * @param categoryId     类目编号
     * @param operationType  操作类型
     * @param operationUsers 操作用户【多个以逗号分隔】
     * @return 包含操作用户的类目属性
     * @since 1.0.0
     */
    public static FleaCategoryAttr newIncludeOperationUsersAttr(Long categoryId, String operationType, String operationUsers) {
        // 【{0}】包含操作用户
        return newUsersAttr(categoryId, operationType,
                FleaFSConstants.AttrConstants.ATTR_CODE_INCLUDE_OPERATION_USER, "FLEAFS-CATEGORY00000003", operationUsers);
    }

    /**
     * 新建包含用户组的类目属性
     *
     * @param categoryId    类目编号
     * @param operationType 操作类型
     * @param userGroups    用户组【多个以逗号分隔】
     * @return 包含用户组的类目属性
     * @since 1.0.0
     */
    public static FleaCategoryAttr newIncludeUserGroupsAttr(Long categoryId, String operationType, String userGroups) {
        // 【{0}】包含用户组
        return newUsersAttr(categoryId, operationType,
                FleaFSConstants.AttrConstants.ATTR_CODE_INCLUDE_USER_GROUP, "FLEAFS-CATEGORY00000004", userGroups);
    }

    /**
     * 新建排除系统用户的类目属性
     *
     * @param categoryId    类目编号
     * @param operationType 操作类型
     * @param systemUsers   系统用户【多个以逗号分隔】
     * @return 排除系统用户的类目属性
     * @since 1.0.0
     */
    public static FleaCategoryAttr newExcludeSystemUsersAttr(Long categoryId, String operationType, String systemUsers) {
        // 【{0}】排除系统用户
        return newUsersAttr(categoryId, operationType,
                FleaFSConstants.AttrConstants.ATTR_CODE_EXCLUDE_SYSTEM_USER, "FLEAFS-CATEGORY00000005", systemUsers);
    }

    /**
     * 新建排除操作用户的类目属性
     *
     * @param categoryId     类目编号
     * @param operationType  操作类型
     * @param operationUsers 操作用户【多个以逗号分隔】
     * @return 排除操作用户的类目属性
     * @since 1.0.0
     */
    public static FleaCategoryAttr newExcludeOperationUsersAttr(Long categoryId, String operationType, String operationUsers) {
        // 【{0}】排除操作用户
        return newUsersAttr(categoryId, operationType,
                FleaFSConstants.AttrConstants.ATTR_CODE_EXCLUDE_OPERATION_USER, "FLEAFS-CATEGORY00000006", operationUsers);
    }

    /**
     * 新建排除用户组的类目属性
     *
     * @param categoryId    类目编号
     * @param operationType 操作类型
     * @param userGroups    用户组【多个以逗号分隔】
     * @return 排除用户组的类目属性
     * @since 1.0.0
     */
    public static FleaCategoryAttr newExcludeUserGroupsAttr(Long categoryId, String operationType, String userGroups) {
        // 【{0}】排除用户组
        return newUsersAttr(categoryId, operationType,
                FleaFSConstants.AttrConstants.ATTR_CODE_EXCLUDE_USER_GROUP, "FLEAFS-CATEGORY00000007", userGroups);
    }

    /**
     * 新建用户名单的类目属性
     *
     * @param categoryId    类目编号
     * @param operationType 操作类型
     * @param attrCode      属性码
     * @param i18nCode      属性描述国际化键
     * @param users         用户或用户组【多个以逗号分隔】
     * @return 用户名单的类目属性
     * @since 1.0.0
     */
    private static FleaCategoryAttr newUsersAttr(Long categoryId, String operationType, String attrCode, String i18nCode, String users) {
        String newAttrCode = newAttrCode(attrCode, operationType);
        String attrDesc = FleaI18nHelper.i18nForCommon(i18nCode, generateValues(operationType));
        // 配置的用户或用户组才允许访问，多个以逗号分隔
        return new FleaCategoryAttr(categoryId, newAttrCode, users, attrDesc, "");
    }

    /**
     * 新建带操作类型后缀的类目属性码
     *
     * @param attrCode      属性码
     * @param operationType 操作类型
     * @return 类目属性码
     * @since 1.0.0
     */
    private static String newAttrCode(String attrCode, String operationType) {
        if (StringUtils.isNotBlank(operationType)) {
            attrCode += CommonConstants.SymbolConstants.UNDERLINE + operationType;
        }
        return attrCode;
    }

    private static String[] generateValues(String operationType) {
        String[] values = null;
        if (StringUtils.isNotBlank(operationType)) {
            try {
                Integer oType = Integer.valueOf(operationType);
                OperateTypeEnum operateTypeEnum = OperateTypeEnum.values()[oType - 1];
                values = new String[]{operateTypeEnum.getName()};
            } catch (Exception e) {
                LOGGER.error1(new Object() {}, "[operationType = {}] is invalid", operationType);
                ExceptionUtils.throwFleaException(FleaFSException.class, "[operationType = {}] is invalid");
            }
        } else {
            // 默认
            values = new String[]{FleaI18nHelper.i18nForCommon("FLEAFS-CATEGORY00000000")};
        }
        return values;
    }
}
