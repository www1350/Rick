package com.absurd.rick.exception;

/**
 * Created by wangwenwei on 2017/10/17.
 */
public class RickException extends BaseException{

    public RickException(ErrorCodeEnum errorCodeEnum, Throwable e) {
        super(errorCodeEnum, e);
    }

    public RickException(String code, String msg, Throwable e) {
        super(code, msg, e);
    }
}
