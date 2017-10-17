package com.absurd.rick.akka.eventbus;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class MsgEnvelope {
    public final String topic;
    public final Object payload;

    public MsgEnvelope(String topic, Object payload) {
        this.topic = topic;
        this.payload = payload;
    }
}
