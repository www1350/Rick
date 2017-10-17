package com.absurd.rick.akka.eventbus;

import akka.actor.ActorRef;
import akka.event.japi.ScanningEventBus;

/**
 * Publishes String messages with length less than or equal to the length
 * specified when subscribing.
 * Created by wangwenwei on 2017/10/10.
 */
public class ScanningBusImpl extends ScanningEventBus<String, ActorRef, Integer> {

    // is needed for determining matching classifiers and storing them in an
    // ordered collection
    @Override public int compareClassifiers(Integer a, Integer b) {
        return a.compareTo(b);
    }

    // is needed for storing subscribers in an ordered collection
    @Override public int compareSubscribers(ActorRef a, ActorRef b) {
        return a.compareTo(b);
    }

    // determines whether a given classifier shall match a given event; it is invoked
    // for each subscription for all received events, hence the name of the classifier
    @Override public boolean matches(Integer classifier, String event) {
        return event.length() <= classifier;
    }

    // will be invoked for each event for all subscribers which registered themselves
    // for the event’s classifier
    @Override public void publish(String event, ActorRef subscriber) {
        subscriber.tell(event, ActorRef.noSender());
    }

}

