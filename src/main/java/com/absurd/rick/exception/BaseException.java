package com.absurd.rick.exception;

/**
 * Created by wangwenwei on 2017/10/17.
 */
public class BaseException extends RuntimeException{
    private String code;

    private String msg;


    private Throwable           exception;

    public BaseException(ErrorCodeEnum errorCodeEnum, Throwable e) {
        this.code = errorCodeEnum.getCode();
        this.msg = errorCodeEnum.getMsg();
        this.exception = e;
    }

    public BaseException(String code, String msg, Throwable e) {
        this.code = code;
        this.msg = msg;
        this.exception = e;
    }




}
