package com.summer.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Summer
 * @date 2022/4/14 1:05
 */
@AllArgsConstructor
@Getter
public enum BizCodeEnum {
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败");
    private final Integer code;
    private final String msg;

//    BizCodeEnum(Integer code, String msg) {
//        this.code = code;
//        this.msg = msg;
//    }
}
