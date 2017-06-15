package com.absurd.rick.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * Created by wangwenwei on 17/6/11.
 */
@ControllerAdvice("com.absurd.rick.controller")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice{
    public JsonpAdvice() {
        super("callback","jsonp");
    }
}
