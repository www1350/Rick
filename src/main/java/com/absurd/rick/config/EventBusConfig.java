package com.absurd.rick.config;

import com.absurd.rick.exception.ErrorCodeEnum;
import com.absurd.rick.exception.RickException;
import com.absurd.rick.util.Global;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Slf4j
@Configuration
public class EventBusConfig {
    @Bean
    public EventBus eventBus(){
        return new EventBus((exception, context) -> {
            log.error("同步消息总线异常: [subscribeMethod={}, event={} ]",context.getSubscriberMethod(),context.getEvent().toString(),exception);
            throw new RickException(ErrorCodeEnum.BUS_ERROR.getCode(), ErrorCodeEnum.BUS_ERROR.getMsg(), exception);

        });
    }

    @Bean
    public AsyncEventBus asyncEventBus(){
        return new AsyncEventBus(Global.EXECUTOR_SERVICE, (exception, context) -> log.error("异步消息队列异常: [subscribeMethod={}, event={} ]",context.getSubscriberMethod(), context.getEvent().toString(),exception));
    }
}
