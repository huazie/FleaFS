package com.huazie.ffs.module.auth.common;

import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;

/**
 * FleaFS 文件类目定位器
 *
 * <p> 请求业务报文中并不直接携带文件类目编号，而各文件管理服务的入参结构又各不相同：
 * <ul>
 *     <li>鉴权类服务：入参直接携带文件类目编号或文件编号</li>
 *     <li>文件操作类服务：入参携带鉴权令牌，由令牌关联的鉴权信息中取文件类目编号</li>
 * </ul>
 * 因此将【如何从业务入参定位文件类目】下沉到各文件管理服务的实现类中，按需定制。
 *
 * <p> 未实现本接口的文件管理操作【如文件搜索，属跨类目检索、无类目上下文】，不做类目级校验。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFileCategoryLocator {

    /**
     * 支持的文件管理操作类型
     *
     * @return 文件管理操作类型
     * @since 1.0.0
     */
    OperateTypeEnum getOperateType();

    /**
     * 从业务入参对象中定位文件类目编号
     *
     * @param inputObj 业务入参对象
     * @return 文件类目编号，未定位到时返回null
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    Long getCategoryId(Object inputObj) throws CommonException;
}
