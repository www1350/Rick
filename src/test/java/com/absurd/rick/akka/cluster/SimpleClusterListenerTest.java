package com.absurd.rick.akka.cluster;

import akka.actor.ActorSystem;
import akka.cluster.Cluster;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;

/**
 * Created by wangwenwei on 2017/10/22.
 */
public class SimpleClusterListenerTest {
    @Test
    public void join(){

        startup("2551");
        startup("2552");


    }

    private void startup(String port) {
        Config config = ConfigFactory.parseString(
                "akka.remote.netty.tcp.port=" + port).withFallback(
                ConfigFactory.load());
        final ActorSystem system = ActorSystem.create("ClusterSystem", config);


        // Create an actor that handles cluster domain events
        system.actorOf(SimpleClusterListener.props(),
                "clusterListener");

//        final Cluster cluster = Cluster.get(system);
//        cluster.leave(cluster.selfAddress());
    }


}
