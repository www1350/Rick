package com.absurd.rick.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by wangwenwei on 2017/8/21.
 */
public class MyActorTest {
    @Test
    public void actor(){
        final ActorSystem system = ActorSystem.create("helloakka");

        try {
            final ActorRef howdyGreeter =
                    system.actorOf(MyActor.props());
            howdyGreeter.tell("2",ActorRef.noSender());
            howdyGreeter.tell(1,ActorRef.noSender());
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            system.terminate();
        }
    }
}
