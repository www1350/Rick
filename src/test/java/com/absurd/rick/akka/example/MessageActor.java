package com.absurd.rick.akka.example;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by wangwenwei on 2017/10/19.
 */
public class MessageActor extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(){
        return Props.create(MessageActor.class,MessageActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class,m->{
                    log.info("message {} id {}",m.getData(),m.getId());
                })
                .matchAny(o ->{
                    log.info("other {}",o);
                })
                .build();
    }
}
