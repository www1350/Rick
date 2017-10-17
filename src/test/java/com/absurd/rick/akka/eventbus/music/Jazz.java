package com.absurd.rick.akka.eventbus.music;

import lombok.Data;
import lombok.ToString;

/**
 * Created by wangwenwei on 2017/10/10.
 */
@ToString
public class Jazz implements AllKindsOfMusic {
    final public String artist;
    public Jazz(String artist) {
        this.artist = artist;
    }
}
