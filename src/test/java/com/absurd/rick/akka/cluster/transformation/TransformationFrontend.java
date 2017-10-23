package com.absurd.rick.akka.cluster.transformation;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;

import java.util.ArrayList;
import java.util.List;

import static com.absurd.rick.akka.cluster.transformation.TransformationMessages.BACKEND_REGISTRATION;

/**
 * Created by wangwenwei on 2017/10/22.
 */
public class TransformationFrontend extends AbstractActor {

    List<ActorRef> backends = new ArrayList<ActorRef>();
    int jobCounter = 0;

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(TransformationMessages.TransformationJob.class, job -> backends.isEmpty(), job -> {
                    getSender().tell(
                            new TransformationMessages.JobFailed("Service unavailable, try again later", job),
                            getSender());
                })
                .match(TransformationMessages.TransformationJob.class, job -> {
                    jobCounter++;
                    backends.get(jobCounter % backends.size())
                            .forward(job, getContext());
                })
                .matchEquals(BACKEND_REGISTRATION, x -> {
                    getContext().watch(getSender());
                    backends.add(getSender());
                })
                .match(Terminated.class, terminated -> {
                    backends.remove(terminated.getActor());
                })
                .build();
    }

}