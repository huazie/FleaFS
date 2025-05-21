package com.huazie.ffs.base.service.interfaces;

import com.huazie.ffs.base.entity.FleaTokenInfo;
import com.huazie.ffs.common.OperateTypeEnum;
import com.huazie.fleaframework.common.exceptions.CommonException;
import com.huazie.fleaframework.db.jpa.service.interfaces.IAbstractFleaJPASV;

import java.util.Map;

/**
 * Flea鉴权信息SV层接口定义
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaTokenInfoSV extends IAbstractFleaJPASV<FleaTokenInfo> {

    /**
     * 生成鉴权Token
     *
     * @return 鉴权Token
     * @since 1.0.0
     */
    String generateToken();

    /**
     * 生成token鉴权信息
     *
     * @param token           鉴权token
     * @param fileId          文件编号
     * @param categoryId      类目编号
     * @param operateTypeEnum 操作类型
     * @param extendMap       扩展Map
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    void saveFleaTokenInfo(String token, String fileId, Long categoryId, OperateTypeEnum operateTypeEnum, Map<String, Object> extendMap) throws CommonException;

    /**
     * 查询有效的Flea鉴权信息
     *
     * @param token 鉴权token
     * @return Flea鉴权信息
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    FleaTokenInfo queryValidFleaTokenInfo(String token) throws CommonException;
}