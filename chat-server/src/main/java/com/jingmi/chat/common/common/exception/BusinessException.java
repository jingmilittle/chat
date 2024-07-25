package com.jingmi.chat.common.common.exception;

import com.jingmi.chat.common.common.domain.enums.CommonErrorEnum;
import lombok.Data;

import static com.jingmi.chat.common.common.domain.enums.CommonErrorEnum.BUSINESS_ERROR;

/**
 * @program: jingmiChat
 * @description: 业务异常
 * @author: JingMi
 * @create: 2024-07-21 13:54
 **/
@Data
public class BusinessException extends RuntimeException{
    protected Integer errorCode;
    protected String errorMsg;
    public BusinessException(Integer errorCode, String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public BusinessException( String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = BUSINESS_ERROR.getCode();
    }

    public BusinessException(CommonErrorEnum errorEnum ){
        super(errorEnum.getMessage());
        this.errorMsg = errorEnum.getMessage();
        this.errorCode = errorEnum.getCode();

    }

}
