package com.absurd.rick.config;

import com.absurd.rick.annotation.EventSubscribe;
import com.absurd.rick.event.Handler;
import com.absurd.rick.event.support.guava.EventHandlerProxy;
import com.absurd.rick.event.support.guava.ThreadSafeEventHandlerProxy;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Component
public class EventPostProcessor implements BeanPostProcessor{
    @Autowired
    private EventBus eventBus;

    @Autowired
    private AsyncEventBus asyncEventBus;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getFields();
        Method[] methods = bean.getClass().getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return bean;
        }
        for (Method method : methods){
            EventSubscribe subscribe =  method.getAnnotation(EventSubscribe.class);
            if (subscribe == null) {
                continue;
            }
            Handler eventHandlerProxy = null;
            if (subscribe.threadSafe()){
                eventHandlerProxy = new ThreadSafeEventHandlerProxy((Handler) bean);
            }else{
                eventHandlerProxy = new EventHandlerProxy((Handler) bean);
            }

            eventBus.register(eventHandlerProxy);
            asyncEventBus.register(eventHandlerProxy);
        }



        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return bean;
        }
        return bean;
    }
}
