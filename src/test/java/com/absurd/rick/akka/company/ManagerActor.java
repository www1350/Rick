package com.absurd.rick.akka.company;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.absurd.rick.akka.company.message.Confirm;
import com.absurd.rick.akka.company.message.DoAction;
import com.absurd.rick.akka.company.message.Meeting;

/**
 * Created by wangwenwei on 2017/10/19.
 */
public class ManagerActor extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(ManagerActor.class, () -> new ManagerActor());
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Meeting.class, m->{
                    log.info(" {} 加入会议",self().path());
                    log.info("老板说 {}",m.getContent());
                        sender().tell(
                                new Confirm("老板，我知道了",self().path()),
                                self());
                })
                .match(DoAction.class,d->{
                    log.info("分配工作");
                    ActorRef workerActorRef = context().actorOf(WorkerActor.props(), "worker");
                    workerActorRef.forward(d,context());
                })
                .build();
    }
}
