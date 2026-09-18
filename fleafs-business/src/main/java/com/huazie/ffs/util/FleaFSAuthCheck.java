package com.huazie.ffs.util;

import com.huazie.ffs.base.service.interfaces.IFleaCategoryAttrSV;
import com.huazie.ffs.common.FleaFSConstants;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.fleaframework.auth.base.user.entity.FleaUserGroupRel;
import com.huazie.fleaframework.auth.base.user.service.interfaces.IFleaUserGroupRelSV;
import com.huazie.fleaframework.auth.cache.bean.FleaAuthCache;
import com.huazie.fleaframework.auth.common.AuthRelTypeEnum;
import com.huazie.fleaframework.auth.common.pojo.user.FleaUserModuleData;
import com.huazie.fleaframework.auth.util.FleaAuthCheck;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.FleaApplicationContext;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.db.common.exceptions.ServiceException;

import java.util.ArrayList;
import java.util.List;

/**
 * FleaFS 文件管理授权校验工具类
 *
 * <p> 授权校验方式取自文件类目属性【AUTH_CHECK_MODE】，按位组合：
 * <ul>
 *     <li>bit0（1）：系统用户授权校验</li>
 *     <li>bit1（2）：操作用户授权校验</li>
 * </ul>
 * 校验方式支持按文件管理操作类型单独配置，即优先取【AUTH_CHECK_MODE_操作序号】，
 * 未配置时回退取【AUTH_CHECK_MODE】通用配置；两者均未配置时，视为类目配置缺失，直接拒绝操作。
 *
 * <p> 每个授权校验维度均包含两类名单：
 * <ul>
 *     <li>包含名单【INCLUDE_xxx】：配置后，账户（或账户所属用户组）必须命中其中之一</li>
 *     <li>排除名单【EXCLUDE_xxx】：账户（或账户所属用户组）命中即拒绝，优先级高于包含名单</li>
 * </ul>
 * 其中用户组名单同时作用于系统用户与操作用户两个维度，用于按用户组批量授权。
 *
 * <p> 涉及的账户与用户组数据均取自 flea-framework 的 flea-auth 模块。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaFSAuthCheck {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaFSAuthCheck.class);

    private static volatile IFleaCategoryAttrSV categoryAttrSV;

    private static volatile IFleaUserGroupRelSV userGroupRelSV;

    private static volatile FleaAuthCache fleaAuthCache;

    private FleaFSAuthCheck() {
    }

    /**
     * 文件管理授权校验
     *
     * @param categoryId      文件类目编号
     * @param operateTypeEnum 文件管理操作类型
     * @param systemAccountId 系统账户编号
     * @param accountId       操作账户编号
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    public static void checkFileAuth(Long categoryId, OperateTypeEnum operateTypeEnum, Long systemAccountId, Long accountId) throws CommonException {
        Object obj = new Object() {};
        LOGGER.debug1(obj, "FleaFS File Auth Check, Start, categoryId = {}, operateType = {}, systemAccountId = {}, accountId = {}",
                categoryId, ObjectUtils.isEmpty(operateTypeEnum) ? null : operateTypeEnum.getName(), systemAccountId, accountId);

        // 获取文件类目配置的授权校验方式
        String authCheckMode = getCategoryAttrValue(categoryId, FleaFSConstants.AttrConstants.ATTR_CODE_AUTH_CHECK_MODE, operateTypeEnum);
        if (StringUtils.isBlank(authCheckMode)) {
            // ERROR-SERVICE0000000014 文件类目【{0}】未配置【{1}】操作的授权校验方式，请检查！
            throw new ServiceException("ERROR-SERVICE0000000014", StringUtils.valueOf(categoryId), getOperateTypeName(operateTypeEnum));
        }

        int checkMode = parseAuthCheckMode(authCheckMode, categoryId);
        if (FleaFSConstants.AuthConstants.AUTH_CHECK_MODE_NONE == checkMode) {
            LOGGER.debug1(obj, "FleaFS File Auth Check, No Need To Check");
            return;
        }

        // #1. 系统用户授权校验
        if (isCheckMode(checkMode, FleaFSConstants.AuthConstants.AUTH_CHECK_MODE_SYSTEM_USER)) {
            checkAccountAuth(categoryId, operateTypeEnum, systemAccountId,
                    FleaFSConstants.AttrConstants.ATTR_CODE_INCLUDE_SYSTEM_USER,
                    FleaFSConstants.AttrConstants.ATTR_CODE_EXCLUDE_SYSTEM_USER);
        }

        // #2. 操作用户授权校验
        if (isCheckMode(checkMode, FleaFSConstants.AuthConstants.AUTH_CHECK_MODE_OPERATION_USER)) {
            checkAccountAuth(categoryId, operateTypeEnum, accountId,
                    FleaFSConstants.AttrConstants.ATTR_CODE_INCLUDE_OPERATION_USER,
                    FleaFSConstants.AttrConstants.ATTR_CODE_EXCLUDE_OPERATION_USER);
        }

        LOGGER.debug1(obj, "FleaFS File Auth Check, End");
    }

    /**
     * 校验指定账户是否在文件类目配置的授权范围内
     *
     * @param categoryId      文件类目编号
     * @param operateTypeEnum 文件管理操作类型
     * @param accountId       待校验的账户编号
     * @param includeAttrCode 包含名单属性码
     * @param excludeAttrCode 排除名单属性码
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private static void checkAccountAuth(Long categoryId, OperateTypeEnum operateTypeEnum, Long accountId,
                                         String includeAttrCode, String excludeAttrCode) throws CommonException {
        // 校验账户编号不能为空
        FleaAuthCheck.checkAccountId(accountId);

        Object obj = new Object() {};
        LOGGER.debug1(obj, "FleaFS File Auth Check, accountId = {}, includeAttr = {}, excludeAttr = {}",
                accountId, includeAttrCode, excludeAttrCode);

        // 获取账户对应的用户模块数据【取自 flea-auth】
        FleaUserModuleData userModuleData = getFleaAuthCache().getFleaUserModuleData(accountId);
        // 校验Flea账户与Flea用户是否存在
        FleaAuthCheck.checkFleaUserModuleData(userModuleData, StringUtils.valueOf(accountId));

        // 账户关联的用户编号
        Long userId = userModuleData.getFleaUser().getUserId();
        // 用户所属的用户组编号集
        List<Long> userGroupIds = getUserGroupIds(userId);

        // #1. 排除名单校验【命中即拒绝，优先级高于包含名单】
        String excludeUsers = getCategoryAttrValue(categoryId, excludeAttrCode, operateTypeEnum);
        String excludeUserGroups = getCategoryAttrValue(categoryId, FleaFSConstants.AttrConstants.ATTR_CODE_EXCLUDE_USER_GROUP, operateTypeEnum);
        if (containsAccount(excludeUsers, accountId) || containsAnyUserGroup(userGroupIds, excludeUserGroups)) {
            // ERROR-SERVICE0000000015 账户【{0}】已被文件类目【{1}】的【{2}】操作排除，不允许访问！
            throw new ServiceException("ERROR-SERVICE0000000015", StringUtils.valueOf(accountId),
                    StringUtils.valueOf(categoryId), getOperateTypeName(operateTypeEnum));
        }

        // #2. 包含名单校验【配置了名单则必须命中】
        String includeUsers = getCategoryAttrValue(categoryId, includeAttrCode, operateTypeEnum);
        String includeUserGroups = getCategoryAttrValue(categoryId, FleaFSConstants.AttrConstants.ATTR_CODE_INCLUDE_USER_GROUP, operateTypeEnum);
        if ((StringUtils.isNotBlank(includeUsers) || StringUtils.isNotBlank(includeUserGroups))
                && !containsAccount(includeUsers, accountId) && !containsAnyUserGroup(userGroupIds, includeUserGroups)) {
            // ERROR-SERVICE0000000016 账户【{0}】不在文件类目【{1}】的【{2}】操作授权范围内，不允许访问！
            throw new ServiceException("ERROR-SERVICE0000000016", StringUtils.valueOf(accountId),
                    StringUtils.valueOf(categoryId), getOperateTypeName(operateTypeEnum));
        }
    }

    /**
     * 获取文件类目属性值【按操作类型优先，未配置时回退取通用配置】
     *
     * @param categoryId      文件类目编号
     * @param attrCode        属性码
     * @param operateTypeEnum 文件管理操作类型
     * @return 属性值，未配置时返回 null
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private static String getCategoryAttrValue(Long categoryId, String attrCode, OperateTypeEnum operateTypeEnum) throws CommonException {
        if (ObjectUtils.isEmpty(categoryId)) return null;

        String attrValue = null;
        if (ObjectUtils.isNotEmpty(operateTypeEnum)) {
            // 操作类型序号从 1 开始，与类目属性后缀保持一致
            String operateType = StringUtils.valueOf(operateTypeEnum.getIndex() + 1);
            attrValue = getCategoryAttrSV().queryCategoryAttrValue(categoryId,
                    StringUtils.strCat(attrCode, CommonConstants.SymbolConstants.UNDERLINE, operateType));
        }
        if (StringUtils.isBlank(attrValue)) {
            attrValue = getCategoryAttrSV().queryCategoryAttrValue(categoryId, attrCode);
        }
        return attrValue;
    }

    /**
     * 解析授权校验方式
     *
     * @param authCheckMode 授权校验方式配置值
     * @param categoryId    文件类目编号
     * @return 授权校验方式位值
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private static int parseAuthCheckMode(String authCheckMode, Long categoryId) throws CommonException {
        try {
            return Integer.parseInt(StringUtils.trim(authCheckMode));
        } catch (NumberFormatException e) {
            LOGGER.error1(new Object() {}, "Parse Auth Check Mode Error, authCheckMode = {}", authCheckMode);
            // ERROR-SERVICE0000000017 文件类目【{0}】配置的授权校验方式【{1}】非法，请检查！
            throw new ServiceException("ERROR-SERVICE0000000017", StringUtils.valueOf(categoryId), authCheckMode);
        }
    }

    /**
     * 校验授权校验方式位是否命中【按位与】
     *
     * @param checkMode    授权校验方式
     * @param checkModeBit 授权校验方式位
     * @return true：命中 false：未命中
     * @since 1.0.0
     */
    private static boolean isCheckMode(int checkMode, int checkModeBit) {
        return (checkMode & checkModeBit) == checkModeBit;
    }

    /**
     * 获取用户所属的用户组编号集【取自 flea-auth】
     *
     * @param userId 用户编号
     * @return 用户组编号集
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private static List<Long> getUserGroupIds(Long userId) throws CommonException {
        List<Long> userGroupIds = new ArrayList<>();
        if (ObjectUtils.isEmpty(userId)) return userGroupIds;

        List<FleaUserGroupRel> userGroupRelList = getUserGroupRelSV().getUserGroupRelList(null, userId,
                AuthRelTypeEnum.USER_GROUP_REL_USER.getRelType());
        if (CollectionUtils.isNotEmpty(userGroupRelList)) {
            for (FleaUserGroupRel userGroupRel : userGroupRelList) {
                userGroupIds.add(userGroupRel.getUserGroupId());
            }
        }
        return userGroupIds;
    }

    /**
     * 校验配置值中是否包含指定的账户编号【多个以逗号分隔】
     *
     * @param attrValue 配置值
     * @param accountId 账户编号
     * @return true：包含 false：不包含
     * @since 1.0.0
     */
    private static boolean containsAccount(String attrValue, Long accountId) {
        if (StringUtils.isBlank(attrValue) || ObjectUtils.isEmpty(accountId)) return false;

        String accountIdStr = StringUtils.valueOf(accountId);
        String[] values = StringUtils.split(attrValue, CommonConstants.SymbolConstants.COMMA);
        for (String value : values) {
            if (StringUtils.isNotBlank(value) && accountIdStr.equals(StringUtils.trim(value))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验配置值中是否包含指定的用户组编号集【多个以逗号分隔】
     *
     * @param userGroupIds 用户组编号集
     * @param attrValue    配置值
     * @return true：包含 false：不包含
     * @since 1.0.0
     */
    private static boolean containsAnyUserGroup(List<Long> userGroupIds, String attrValue) {
        if (CollectionUtils.isEmpty(userGroupIds) || StringUtils.isBlank(attrValue)) return false;

        String[] values = StringUtils.split(attrValue, CommonConstants.SymbolConstants.COMMA);
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                try {
                    if (userGroupIds.contains(Long.valueOf(StringUtils.trim(value)))) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error1(new Object() {}, "Parse User Group Id Error, value = {}", value);
                }
            }
        }
        return false;
    }

    /**
     * 获取文件管理操作名称
     *
     * @param operateTypeEnum 文件管理操作类型
     * @return 操作名称
     * @since 1.0.0
     */
    private static String getOperateTypeName(OperateTypeEnum operateTypeEnum) {
        return ObjectUtils.isEmpty(operateTypeEnum) ? null : operateTypeEnum.getName();
    }

    private static IFleaCategoryAttrSV getCategoryAttrSV() {
        if (ObjectUtils.isEmpty(categoryAttrSV)) {
            synchronized (FleaFSAuthCheck.class) {
                if (ObjectUtils.isEmpty(categoryAttrSV)) {
                    categoryAttrSV = FleaApplicationContext.getBean(IFleaCategoryAttrSV.class);
                }
            }
        }
        return categoryAttrSV;
    }

    private static IFleaUserGroupRelSV getUserGroupRelSV() {
        if (ObjectUtils.isEmpty(userGroupRelSV)) {
            synchronized (FleaFSAuthCheck.class) {
                if (ObjectUtils.isEmpty(userGroupRelSV)) {
                    userGroupRelSV = FleaApplicationContext.getBean(IFleaUserGroupRelSV.class);
                }
            }
        }
        return userGroupRelSV;
    }

    private static FleaAuthCache getFleaAuthCache() {
        if (ObjectUtils.isEmpty(fleaAuthCache)) {
            synchronized (FleaFSAuthCheck.class) {
                if (ObjectUtils.isEmpty(fleaAuthCache)) {
                    fleaAuthCache = FleaApplicationContext.getBean(FleaAuthCache.class);
                }
            }
        }
        return fleaAuthCache;
    }
}
