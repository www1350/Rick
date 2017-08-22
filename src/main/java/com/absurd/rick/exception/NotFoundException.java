package com.absurd.rick.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by wangwenwei on 2017/8/22.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}