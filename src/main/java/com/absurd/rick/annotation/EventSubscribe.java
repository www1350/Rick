package com.absurd.rick.annotation;

import java.lang.annotation.*;

/**
 * Created by wangwenwei on 17/8/3.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface EventSubscribe {
    boolean threadSafe() default false;
}
