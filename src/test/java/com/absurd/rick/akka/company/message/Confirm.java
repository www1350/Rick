package com.absurd.rick.akka.company.message;

import akka.actor.ActorPath;
import lombok.Data;

/**
 * Created by wangwenwei on 2017/10/19.
 */
@Data
public class Confirm extends Message{
    private ActorPath actorPath;

    public Confirm(String content, ActorPath actorPath) {
        super(content);
        this.actorPath = actorPath;
    }
}
