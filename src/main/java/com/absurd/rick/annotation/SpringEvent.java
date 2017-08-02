package com.absurd.rick.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD,ElementType.TYPE})
public @interface SpringEvent {
    boolean enable() default true;

    String value();
    
    EventPostEnum advice() default EventPostEnum.AFTER;
}
