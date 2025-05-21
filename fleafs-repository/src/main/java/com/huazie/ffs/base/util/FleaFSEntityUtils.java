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
        String attrCode = FleaFSConstants.AttrConstants.ATTR_CODE_AUTH_CHECK_MODE;
        if (StringUtils.isNotBlank(operationType)) {
            attrCode += CommonConstants.SymbolConstants.UNDERLINE + operationType;
        }
        // 【{0}】授权校验方式
        String attrDesc = FleaI18nHelper.i18nForCommon("FLEAFS-CATEGORY00000001", generateValues(operationType));
        // 授权校验方式描述
        String remarks = FleaI18nHelper.i18nForCommon(FleaFSConstants.FileCategoryConstants.FLEAFS_AUTH_CHECK_MODE + authCheckMode);
        return new FleaCategoryAttr(categoryId, attrCode, authCheckMode, attrDesc, remarks);
    }

    /**
     * 新建包含系统用户的类目属性
     *
     * @param categoryId 类目编号
     * @param operationType 操作类型
     * @param systemUsers 系统用户【多个以逗号分隔】
     * @return 包含系统用户的类目属性
     * @since 1.0.0
     */
    public static FleaCategoryAttr newIncludeSystemUsersAttr(Long categoryId, String operationType, String systemUsers) {
        String attrCode = FleaFSConstants.AttrConstants.ATTR_CODE_INCLUDE_SYSTEM_USER;
        if (StringUtils.isNotBlank(operationType)) {
            attrCode += CommonConstants.SymbolConstants.UNDERLINE + operationType;
        }
        // 【{0}】包含系统用户
        String attrDesc = FleaI18nHelper.i18nForCommon("FLEAFS-CATEGORY00000002", generateValues(operationType));
        // 配置的系统用户才允许访问，多个以逗号分隔；

        return new FleaCategoryAttr(categoryId, attrCode, systemUsers, attrDesc, "");
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
