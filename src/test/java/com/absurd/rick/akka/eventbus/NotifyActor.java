package com.absurd.rick.akka.eventbus;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class NotifyActor extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props() {
        return Props.create(NotifyActor.class, () -> new NotifyActor());
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Notification.class, s -> {
                    log.info("Received String message: {}", s.id);
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
