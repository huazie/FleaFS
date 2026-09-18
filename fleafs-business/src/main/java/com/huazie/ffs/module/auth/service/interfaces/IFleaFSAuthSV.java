package com.huazie.ffs.module.auth.service.interfaces;

import com.huazie.fleaframework.common.exceptions.CommonException;

/**
 * FleaFS 文件管理授权服务接口类
 *
 * <p> 依据请求业务报文中携带的鉴权令牌或文件编号，定位文件所属类目，
 * 依次完成操作启用校验（类目操作状态对应位是否启用该操作）与授权校验
 * （按类目配置的授权校验方式校验系统用户、操作用户）。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaFSAuthSV {

    /**
     * 文件管理授权校验
     *
     * <p> 包含两步类目级校验：操作启用校验、授权校验；
     * 未能定位到文件类目【如文件搜索服务】时，不做任何校验。
     *
     * @param resourceCode    资源编码【对应FleaFS文件管理操作】
     * @param serviceCode     服务编码
     * @param inputJson       请求业务报文JSON串
     * @param systemAccountId 系统账户编号
     * @param accountId       操作账户编号
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    void checkFileAuth(String resourceCode, String serviceCode, String inputJson, Long systemAccountId, Long accountId) throws CommonException;
}
