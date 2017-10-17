package com.absurd.rick.akka.eventbus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.event.Logging;
import akka.testkit.javadsl.TestKit;
import com.absurd.rick.akka.MyActor;
import com.absurd.rick.akka.eventbus.music.*;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class EventBusTest {
    @Test
    public void busTest(){
        LookupBusImpl lookupBus = new LookupBusImpl();
        lookupBus.subscribe(getTestActor(), "greetings");
        lookupBus.publish(new MsgEnvelope("time", System.currentTimeMillis()));
        lookupBus.publish(new MsgEnvelope("greetings", "hello1"));
//        expectMsgEquals("hello");
    }

    final ActorSystem system = ActorSystem.create("eventAkka");

    private ActorRef getTestActor() {
       return system.actorOf(Props.create(MyActor.class, () -> new MyActor()));
    }


    @Test
    public void bus2Test(){
        SubchannelBusImpl subchannelBus = new SubchannelBusImpl();
        subchannelBus.subscribe(getTestActor(), "abc");
        subchannelBus.publish(new MsgEnvelope("xyzabc", "x"));
        subchannelBus.publish(new MsgEnvelope("bcdef", "b"));
        subchannelBus.publish(new MsgEnvelope("abc", "c"));
//        expectMsgEquals("c");
        subchannelBus.publish(new MsgEnvelope("abcdef", "d"));
//        expectMsgEquals("d");
    }

    @Test
    public void bus3Test(){
        ScanningBusImpl scanningBus = new ScanningBusImpl();
        scanningBus.subscribe(getTestActor(), 3);
        scanningBus.publish("xyzabc");
        scanningBus.publish("ab");
//        expectMsgEquals("ab");
        scanningBus.publish("abc");
//        expectMsgEquals("abc");
    }


    @Test
    public void bus4Test(){
        ActorRef observer1 = new TestKit(system).getRef();
        ActorRef observer2 = new TestKit(system).getRef();
        TestKit probe1 = new TestKit(system);
        TestKit probe2 = new TestKit(system);
        ActorRef subscriber1 = probe1.getRef();
        ActorRef subscriber2 = probe2.getRef();
        ActorBusImpl actorBus = new ActorBusImpl(system);
        actorBus.subscribe(subscriber1, observer1);
        actorBus.subscribe(subscriber2, observer1);
        actorBus.subscribe(subscriber2, observer2);
        Notification n1 = new Notification(observer1, 100);
        actorBus.publish(n1);
        probe1.expectMsgEquals(n1);
        probe2.expectMsgEquals(n1);
        Notification n2 = new Notification(observer2, 101);
        actorBus.publish(n2);
        probe2.expectMsgEquals(n2);
        probe1.expectNoMsg(FiniteDuration.create(500, TimeUnit.MILLISECONDS));
    }

    @Test
    public void bus5Test(){
        final ActorSystem system = ActorSystem.create("DeadLetters");
        final ActorRef actor = system.actorOf(Props.create(DeadLetterActor.class));
        system.eventStream().subscribe(actor, DeadLetter.class);
    }


    @Test
    public void bus6Test(){
        final ActorRef actor = system.actorOf(Props.create(DeadLetterActor.class));
        system.eventStream().subscribe(actor, DeadLetter.class);
//        system.eventStream().setLogLevel(Logging.DebugLevel());

//        final ActorRef jazzListener = system.actorOf(Props.create(Listener.class));
        final ActorRef musicListener = system.actorOf(Props.create(Listener.class));
//        system.eventStream().subscribe(jazzListener, Jazz.class);
        system.eventStream().subscribe(musicListener, AllKindsOfMusic.class);

        // only musicListener gets this message, since it listens to *all* kinds of music:
        system.eventStream().publish(new Electronic("Parov Stelar"));
        system.eventStream().publish(new Electronic("Parov fewfwe"));
        system.eventStream().publish(new Electronic("Parov dsa"));
        system.eventStream().publish(new Electronic("Parov dfas"));
        system.eventStream().publish(new Electronic("Parov rgetgr"));
        system.eventStream().publish(new Electronic("Parov few"));
        system.eventStream().publish(new Electronic("Parov vds"));

        // jazzListener and musicListener will be notified about Jazz:
        system.eventStream().publish(new Jazz("Sonny Rollins"));
    }
}
