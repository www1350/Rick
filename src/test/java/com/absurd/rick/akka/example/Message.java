package com.absurd.rick.akka.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wangwenwei on 2017/10/19.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Object data;

    private String id;

    private long dateCreate;
}
