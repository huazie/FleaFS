package com.huazie.ffs.module.update.service.interfaces;

import com.huazie.ffs.pojo.update.input.InputFileUpdateInfo;
import com.huazie.ffs.pojo.update.input.InputUpdateAuthInfo;
import com.huazie.ffs.pojo.update.output.OutputFileUpdateInfo;
import com.huazie.ffs.pojo.update.output.OutputUpdateAuthInfo;
import com.huazie.fleaframework.common.exceptions.CommonException;

/**
 * Flea更新服务接口，提供更新鉴权、文件更新的功能
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaUpdateSV {

    /**
     * 更新授权
     *
     * @param input 更新授权业务入参
     * @return 更新授权业务出参
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    OutputUpdateAuthInfo updateAuth(InputUpdateAuthInfo input) throws CommonException;

    /**
     * 文件更新
     *
     * @param input 文件更新入参（包含更新鉴权令牌）
     * @return 文件更新出参（包含文件编号与当前版本编码）
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    OutputFileUpdateInfo fileUpdate(InputFileUpdateInfo input) throws CommonException;
}
