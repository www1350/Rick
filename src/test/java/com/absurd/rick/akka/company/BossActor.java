package com.absurd.rick.akka.company;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import static akka.pattern.PatternsCS.ask;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import com.absurd.rick.akka.company.message.*;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangwenwei on 2017/10/19.
 */
public class BossActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private int taskCount = 0;

    public static Props props() {
        return Props.create(BossActor.class, () -> new BossActor());
    }


    Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Business.class, b -> {
                    log.info("看准了商机!行动!");
                    log.info("我们是 {} 公司",self().path().address());
                    List managers = new ArrayList<>();
                    ActorRef managerActorRef = context().actorOf(ManagerActor.props(), "managerA"); //这里我们召唤3个主管
                    ActorRef managerActorRef2 = context().actorOf(ManagerActor.props(), "managerB"); //这里我们召唤3个主管
                    ActorRef managerActorRef3 = context().actorOf(ManagerActor.props(), "managerC"); //这里我们召唤3个主管
                    managers.add(managerActorRef);
                    managers.add(managerActorRef2);
                    managers.add(managerActorRef3);
                    Iterator<ActorRef> iterator = managers.iterator();
                    while(iterator.hasNext()){
                        ActorRef managerActor = iterator.next();
                        CompletableFuture<Object> future =
                                ask(managerActor, new Meeting("开会讨论一下"), t)
                                        .toCompletableFuture();
                        CompletableFuture<Confirm> transformed = CompletableFuture.allOf(future)
                                    .thenApply(v -> {
                                        Confirm x = (Confirm) future.join();
                                        return x;
                                    });
                        Confirm confirm =  transformed.get();
                        log.info("{} ---{}",confirm.getContent(),confirm.getActorPath().parent().toString());
                        ActorSelection manager = context().actorSelection(confirm.getActorPath());
                        manager.tell(new DoAction("展开业务"),self());
                    }
                })
                .match(Done.class , d -> {
                    log.info("? {}",d.getContent());
                    taskCount++;
                    if (taskCount == 3) {
                        log.info("项目做完了，涨工资！");
                        context().system().terminate();
                    }
                })
            .build();
    }
}