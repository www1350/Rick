package com.absurd.rick.akka.cluster.transformation;

import akka.actor.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;

import static com.absurd.rick.akka.cluster.transformation.TransformationMessages.BACKEND_REGISTRATION;

/**
 * Created by wangwenwei on 2017/10/22.
 */
public class TransformationBackend extends AbstractActor {

    Cluster cluster = Cluster.get(getContext().getSystem());

    //subscribe to cluster changes, MemberUp
    @Override
    public void preStart() {
        cluster.subscribe(getSelf(), ClusterEvent.MemberUp.class);
    }

    //re-subscribe when restart
    @Override
    public void postStop() {
        cluster.unsubscribe(getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TransformationMessages.TransformationJob.class, job -> {
                    getSender().tell(new TransformationMessages.TransformationResult(job.getText().toUpperCase()),
                            getSelf());
                })
                .match(ClusterEvent.CurrentClusterState.class, state -> {
                    for (Member member : state.getMembers()) {
                        if (member.status().equals(MemberStatus.up())) {
                            register(member);
                        }
                    }
                })
                .match(ClusterEvent.MemberUp.class, mUp -> {
                    register(mUp.member());
                })
                .build();
    }

    void register(Member member) {
        if (member.hasRole("frontend"))
            getContext().actorSelection(member.address() + "/user/frontend").tell(
                    BACKEND_REGISTRATION, getSelf());
    }
}
