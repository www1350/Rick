package com.absurd.rick.akka.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.junit.Test;

/**
 * Created by wangwenwei on 2017/10/19.
 */
public class ExampleMessageTest {
    @Test
    public void sendMessage(){
        final ActorSystem system = ActorSystem.create("sendMessage");
        ActorRef actorRef = system.actorOf(MessageActor.props());

        actorRef.tell(new Message("hello","id1",System.currentTimeMillis()),ActorRef.noSender());
    }
}
