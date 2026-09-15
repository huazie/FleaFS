package com.huazie.ffs.module.version.service.interfaces;

import com.huazie.ffs.pojo.version.input.InputFileVersionInfo;
import com.huazie.ffs.pojo.version.output.OutputFileVersionInfo;
import com.huazie.fleaframework.common.exceptions.CommonException;

/**
 * Flea版本服务接口，提供文件历史版本查询的功能
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IFleaVersionSV {

    /**
     * 查询文件的历史版本列表（按版本编号倒序）
     *
     * @param input 版本查询业务入参
     * @return 版本查询业务出参（包含版本总数与版本明细列表）
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    OutputFileVersionInfo queryFileVersions(InputFileVersionInfo input) throws CommonException;

}
