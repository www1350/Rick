package com.absurd.rick.event.handler;

import com.absurd.rick.config.AuthHolder;
import com.absurd.rick.event.support.Event;
import com.absurd.rick.event.EventSyncExtra;
import com.absurd.rick.event.support.spring.SpringAppEvent;
import com.absurd.rick.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by wangwenwei on 17/8/2.
 */
@Slf4j
@Component
public class SpringEvnetHandler implements ApplicationListener {

    @Async
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof SpringAppEvent){
            SpringAppEvent springEvent = (SpringAppEvent) applicationEvent;
            Event event = springEvent.getEvent();
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
                case "user.info":
                    log.info("{}------>{}",args.get(0),AuthHolder.username());
                default:
                    break;
            }

        }
    }

     void initExtra(Event event){
        Map<String,Object> map =  event.getExtraData();
        Collection<Object> eventSyncs = SpringContextUtil.getBeanByTypeWithCache(EventSyncExtra.class);
        for(Object eventSync : eventSyncs){
            if (eventSync instanceof EventSyncExtra){
                EventSyncExtra eventSyncExtra =  (EventSyncExtra) eventSync;
                eventSyncExtra.setExtra(map.get(eventSync.getClass().getName()));
            }
        }
    }

}
