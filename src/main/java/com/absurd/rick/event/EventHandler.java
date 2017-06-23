package com.absurd.rick.event;

import com.absurd.rick.config.AuthHolder;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Slf4j
@Component
public class EventHandler implements Handler{
    @Subscribe
    @Override
    public void handler(Event event) {
        List<Object> args = event.getData();
        String operator = event.getOperator();
        Map<String,Object> map =  event.getExtraData();
        AuthHolder.set((Map<String, Object>) map.get(AuthEventSyncExtra.class.getName()));
        switch (operator){
            case "auth.login":
                log.info("{},{}",args.get(0),args.get(1));
                log.info(AuthHolder.username());
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
