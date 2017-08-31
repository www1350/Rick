package com.absurd.rick.annotation;

import java.lang.annotation.*;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Inherited
public @interface SpringEvent {
    boolean enable() default true;

    String value();
    
    EventPostEnum advice() default EventPostEnum.AFTER;
}
