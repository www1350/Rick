package com.absurd.rick.util;

import com.absurd.rick.config.AuthHolder;
import com.absurd.rick.filter.RequestFilter;

/**
 * Created by wangwenwei on 17/7/22.
 */
public class ContextUtil {


    public static void clear(){
        RequestFilter.reqJson.remove();
        AuthHolder.clear();
    }
}
