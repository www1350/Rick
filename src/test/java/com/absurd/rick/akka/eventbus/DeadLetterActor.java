package com.absurd.rick.akka.eventbus;

import akka.actor.AbstractActor;
import akka.actor.DeadLetter;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class DeadLetterActor extends AbstractActor {
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(DeadLetter.class, msg -> {
                    System.out.println(msg);
                })
                .build();
    }
}