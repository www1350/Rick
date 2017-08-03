package com.absurd.rick.event.handler;

import com.absurd.rick.annotation.EventSubscribe;
import com.absurd.rick.config.AuthHolder;
import com.absurd.rick.event.support.Event;
import com.absurd.rick.event.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Slf4j
@Component
public class EventHandler implements Handler {
    @EventSubscribe
    @Override
    public void handler(Event event) {
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
