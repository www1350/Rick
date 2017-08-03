package com.absurd.rick.event.support;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Data
public class Event {
    private List<Object> data;

    private String operator;

    private Map<String,Object> extraData;
}
