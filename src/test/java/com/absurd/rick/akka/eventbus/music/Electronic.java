package com.absurd.rick.akka.eventbus.music;

import lombok.ToString;

/**
 * Created by wangwenwei on 2017/10/10.
 */
@ToString
public class Electronic implements AllKindsOfMusic {
    final public String artist;
    public Electronic(String artist) {
        this.artist = artist;
    }
}