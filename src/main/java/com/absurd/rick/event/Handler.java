package com.absurd.rick.event;

import com.google.common.eventbus.Subscribe;

/**
 * Created by wangwenwei on 17/6/23.
 */
public interface Handler {
    @Subscribe
    void handler(Event event);
}
