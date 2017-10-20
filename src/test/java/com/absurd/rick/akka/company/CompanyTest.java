package com.absurd.rick.akka.company;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.absurd.rick.akka.company.message.Business;
import org.junit.Test;

/**
 * Created by wangwenwei on 2017/10/19.
 */
public class CompanyTest {
    @Test
    public void bossActor(){
        //创建一家公司
        final ActorSystem system = ActorSystem.create("company");
        ActorRef bossRef = system.actorOf(BossActor.props(),"boss");
        bossRef.tell(new Business("二手车市场"),ActorRef.noSender());

    }
}
