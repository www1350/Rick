package com.absurd.rick.akka.company;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.absurd.rick.akka.company.message.DoAction;
import com.absurd.rick.akka.company.message.Done;

/**
 * Created by wangwenwei on 2017/10/19.
 */
public class WorkerActor extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public static Props props() {
        return Props.create(WorkerActor.class, () -> new WorkerActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DoAction.class, d->{
                    log.info("小弟开始做事");
                    sender().tell(new Done("完成事情") ,ActorRef.noSender());
                })
                .build();
    }
}
