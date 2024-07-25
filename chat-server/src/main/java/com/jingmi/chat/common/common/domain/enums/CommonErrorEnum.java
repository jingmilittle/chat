package com.jingmi.chat.common.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: jingmiChat
 * @description:
 * @author: JingMi
 * @create: 2024-07-21 13:09
 **/
@AllArgsConstructor
@Getter
public enum CommonErrorEnum {
    BUSINESS_ERROR(0,"{0}"),
    SYSTEM_ERROR(-1,"系统出了小差,请稍后再试"),
    PARAM_INVALID(-2,"参数校验失败"),
    LOCK_LIMIT(-3,"操作太频繁,请稍后再试");
    private final Integer code;
    private final String message;
}
