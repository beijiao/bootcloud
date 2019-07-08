package com.dj.p2p.base;

import lombok.Data;

/**
 * 自定义-业务处理异常类
 */
@Data
public class BusinessException extends RuntimeException {

    private String errorMsg;

    private Integer errorCode;

    public BusinessException() {}

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
