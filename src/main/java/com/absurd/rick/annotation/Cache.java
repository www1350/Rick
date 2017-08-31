package com.absurd.rick.annotation;

import java.lang.annotation.*;


/**
 * @author Absurd.
 */
@Target({ ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface Cache {
    String key() default "";

    //缓存的保存时间//默认一分钟缓存
    int time() default 60;

    Class<?> clazz() default Object.class;

}
