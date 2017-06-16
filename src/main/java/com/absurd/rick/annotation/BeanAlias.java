package com.absurd.rick.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangwenwei on 17/6/16.
 */
@Target({ ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BeanAlias {
    String value() default "";
}
