package com.absurd.rick.akka.eventbus;

import akka.actor.ActorRef;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class Notification {
    public final ActorRef ref;
    public final int id;

    public Notification(ActorRef ref, int id) {
        this.ref = ref;
        this.id = id;
    }
}