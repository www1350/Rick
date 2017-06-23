package com.absurd.rick.event;

import lombok.Data;

import java.util.List;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Data
public class Event {
    private List<Object> data;

    private String operator;
}
