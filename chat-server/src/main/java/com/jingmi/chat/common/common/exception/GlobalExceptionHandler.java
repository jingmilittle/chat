package com.jingmi.chat.common.common.exception;


import com.jingmi.chat.common.common.domain.enums.CommonErrorEnum;
import com.jingmi.chat.common.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: jingmiChat
 * @description: 全局异常捕获
 * @author: JingMi
 * @create: 2024-07-21 12:03
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
    * 校验异常
     */

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> methodArgumentNotValidException(MethodArgumentNotValidException e){
        StringBuffer errorMsg = new StringBuffer();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errorMsg.append(fieldError.getField() + ":" + fieldError.getDefaultMessage() + ";"));
        String message = errorMsg.toString();
        log.error("参数校验异常：{}",e.getMessage());
        return ApiResult.fail(CommonErrorEnum.PARAM_INVALID.getCode(), message);
    }
    /**
    * 业务异常
     */

    @ExceptionHandler(value= BusinessException.class)
    public ApiResult<?> businessExceptionHandler(Throwable e){
        log.info("business Exception：{}",e.getMessage());
        return ApiResult.fail(CommonErrorEnum.BUSINESS_ERROR.getCode(),e.getMessage());
    }

    @ExceptionHandler(value= Throwable.class)
    public ApiResult<?> throwableHandler(Throwable e){
        log.error("系统异常：{}",e.getMessage());
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }

}
