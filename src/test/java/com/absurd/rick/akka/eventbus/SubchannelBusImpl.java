package com.absurd.rick.akka.eventbus;

import akka.actor.ActorRef;
import akka.event.japi.SubchannelEventBus;
import akka.util.Subclassification;

/**
 * Created by wangwenwei on 2017/10/10.
 */
public class SubchannelBusImpl extends SubchannelEventBus<MsgEnvelope, ActorRef, String> {

    // Subclassification is an object providing `isEqual` and `isSubclass`
    // to be consumed by the other methods of this classifier
    @Override public Subclassification<String> subclassification() {
        return new StartsWithSubclassification();
    }

    // is used for extracting the classifier from the incoming events
    @Override public String classify(MsgEnvelope event) {
        return event.topic;
    }

    // will be invoked for each event for all subscribers which registered themselves
    // for the event’s classifier
    @Override public void publish(MsgEnvelope event, ActorRef subscriber) {
        subscriber.tell(event.payload, ActorRef.noSender());
    }

}