package com.absurd.rick.akka.eventbus;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.japi.ManagedActorEventBus;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class ActorBusImpl extends ManagedActorEventBus<Notification> {

    // the ActorSystem will be used for book-keeping operations, such as subscribers terminating
    public ActorBusImpl(ActorSystem system) {
        super(system);
    }

    // is used for extracting the classifier from the incoming events
    @Override public ActorRef classify(Notification event) {
        return event.ref;
    }

    // determines the initial size of the index data structure
    // used internally (i.e. the expected number of different classifiers)
    @Override public int mapSize() {
        return 128;
    }

}