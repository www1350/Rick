package com.absurd.rick.util;

import com.google.common.base.Splitter;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by wangwenwei on 17/6/20.
 */
public class ListUtils {
    public static List<String> splitter(String str, String on){
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return Splitter.on(on).splitToList(str);
    }
}
