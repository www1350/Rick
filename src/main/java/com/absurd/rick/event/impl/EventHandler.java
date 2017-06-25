package com.absurd.rick.event.impl;

import com.absurd.rick.config.AuthHolder;
import com.absurd.rick.event.Event;
import com.absurd.rick.event.Handler;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Slf4j
@Component
public class EventHandler implements Handler {
    @Subscribe
    @Override
    public void handler(Event event) {
        initExtra(event);
        List<Object> args = event.getData();
        String operator = event.getOperator();
        switch (operator){
            case "auth.login":
                log.info("{},{}",args.get(0),args.get(1));
                break;
            case "car.get":
                log.info("{}",args.get(0));
                log.info(AuthHolder.username());
                break;
            default:
                break;
        }

    }
}
