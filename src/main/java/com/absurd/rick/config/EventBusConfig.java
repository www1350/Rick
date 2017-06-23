package com.absurd.rick.config;

import com.absurd.rick.util.Global;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Configuration
public class EventBusConfig {
    @Bean
    public EventBus eventBus(){
        return new EventBus();
    }

    @Bean
    public AsyncEventBus asyncEventBus(){
        return new AsyncEventBus(Global.executors);
    }
}
