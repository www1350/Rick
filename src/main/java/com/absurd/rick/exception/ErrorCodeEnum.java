package com.absurd.rick.exception;

/**
 * Created by wangwenwei on 2017/10/17.
 */
public enum ErrorCodeEnum {
    BUS_ERROR("5005","消息总线异常");
    private String code;

    private String msg;

    ErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
