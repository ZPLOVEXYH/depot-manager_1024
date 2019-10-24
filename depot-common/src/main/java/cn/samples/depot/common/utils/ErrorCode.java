package cn.samples.depot.common.utils;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Description:
 *
 * @className: ErrorCode
 * @Author: zhangpeng
 * @Date 2019/7/16 14:43
 * @Version 1.0
 **/
public enum ErrorCode {

    AUTHENTICATION_FAILED("4000"), USER_FAILED("4001"), JWT_TOKEN_EXPIRED("4002"), USER_NOT_FIND("4003");

    private String errorCode;

    ErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonValue
    public String getErrorCode() {
        return errorCode;
    }
}
