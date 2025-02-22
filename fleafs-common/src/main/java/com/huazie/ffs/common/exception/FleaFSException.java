package com.huazie.ffs.common.exceptions;

import com.huazie.fleaframework.common.exceptions.FleaException;

/**
 * FleaFS 自定义运行时异常
 *
 * @author huazie
 * @version 2.0.0
 * @since 2.0.0
 */
public class FleaFSException extends FleaException {

    private static final long serialVersionUID = -2648647162796453831L;

    public FleaFSException(String message) {
        super(message);
    }

    public FleaFSException(Throwable e) {
        super(e);
    }

    public FleaFSException(String message, Throwable cause) {
        super(message, cause);
    }
}
