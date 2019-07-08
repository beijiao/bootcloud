package com.dj.p2p.wind.config;

import com.dj.p2p.base.BusinessException;
import com.dj.p2p.base.ResultModel;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义异常处理器
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResultModel exceptionHandler(Exception ex) {
        // 打印异常信息 后期可替换到日志框架
        ex.printStackTrace();
        // 异常类型判断
        // 业务处理异常
        if (ex instanceof BusinessException) {
            BusinessException be = (BusinessException) ex;
            return new ResultModel().error(be.getErrorCode(), be.getErrorMsg());
        } else if (ex instanceof IllegalArgumentException) {
            // 参数异常
            return new ResultModel().error(-1, ex.getMessage());
        } else {
            // 其他异常
            return new ResultModel().error(0,"服务器在开小差，请稍后再试");
        }
    }

//    /**
//     * 业务异常处理
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(value = {BusinessException.class})
//    public ResultModel businessExceptionHandler(BusinessException ex) {
//        ex.printStackTrace();
//        return new ResultModel().error(ex.getErrorCode(), ex.getErrorMsg());
//    }
//
//    /**
//     * 参数异常处理
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(value = {IllegalArgumentException.class})
//    public ResultModel illegalArgumentExceptionHandler(IllegalArgumentException ex) {
//        ex.printStackTrace();
//        return new ResultModel().error(-1, ex.getMessage());
//    }
}
