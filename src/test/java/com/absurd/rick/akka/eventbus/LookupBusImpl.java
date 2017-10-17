package com.absurd.rick.akka.eventbus;

import akka.actor.ActorRef;
import akka.event.japi.LookupEventBus;

/**
 * Publishes the payload of the MsgEnvelope when the topic of the
 * MsgEnvelope equals the String specified when subscribing.
 * Created by wangwenwei on 2017/10/10.
 */
public class LookupBusImpl extends LookupEventBus<MsgEnvelope, ActorRef, String> {

    // is used for extracting the classifier from the incoming events
    @Override public String classify(MsgEnvelope event) {
        return event.topic;
    }

    // will be invoked for each event for all subscribers which registered themselves
    // for the event’s classifier
    @Override public void publish(MsgEnvelope event, ActorRef subscriber) {
        subscriber.tell(event.payload, ActorRef.noSender());
    }

    // must define a full order over the subscribers, expressed as expected from
    // `java.lang.Comparable.compare`
    @Override public int compareSubscribers(ActorRef a, ActorRef b) {
        return a.compareTo(b);
    }

    // determines the initial size of the index data structure
    // used internally (i.e. the expected number of different classifiers)
    @Override public int mapSize() {
        return 128;
    }

}