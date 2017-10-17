package com.absurd.rick.akka.eventbus.music;

import akka.actor.AbstractActor;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class Listener extends AbstractActor {
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Jazz.class, msg ->
                        System.out.printf("%s is listening to: %s%n", getSelf().path().name(), msg)
                )
                .match(Electronic.class, msg ->
                        System.out.printf("%s is listening to: %s%n", getSelf().path().name(), msg)
                )
                .build();
    }
}