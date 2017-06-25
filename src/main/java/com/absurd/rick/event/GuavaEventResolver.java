package com.absurd.rick.event;

import com.absurd.rick.annotation.GuavaEvent;
import com.absurd.rick.annotation.GuavaEventPostEnum;
import com.absurd.rick.util.SpringContextUtil;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Aspect
@Component
public class GuavaEventResolver {
    @Autowired
    private EventBus eventBus;
    @Autowired
    private AsyncEventBus asyncEventBus;

    @Pointcut("@annotation(com.absurd.rick.annotation.GuavaEvent)")
    public void execute(){}

    @Before(value = "execute()")
    public void handlerEventBefore(JoinPoint joinPoint){
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        GuavaEvent guavaEvent = method.getAnnotation(GuavaEvent.class);
        if (guavaEvent == null) {
            return;
        }

        if (!guavaEvent.enable()) return;
        if (GuavaEventPostEnum.BEFORE.equals(guavaEvent.advice())){
            postEvent(guavaEvent,joinPoint.getArgs());
        }

    }

    @AfterReturning(value = "execute()")
    public void handlerEventAfter(JoinPoint joinPoint){
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        GuavaEvent guavaEvent = method.getAnnotation(GuavaEvent.class);
        if (guavaEvent == null) {
            return;
        }

        if (!guavaEvent.enable()) return;
        if (GuavaEventPostEnum.AFTER.equals(guavaEvent.advice())){
            postEvent(guavaEvent,joinPoint.getArgs());
        }

    }

    private void postEvent(GuavaEvent guavaEvent, Object[] args) {
        Event event = new Event();
        event.setOperator(guavaEvent.value());
        List<Object> data = new ArrayList<Object>();
        if (args == null || args.length == 0) {
            event.setData(data);
        }
        for (Object arg : args) {
            data.add(arg);
        }
        event.setData(data);
        event.setOperator(guavaEvent.value());
        Map<String, Object> extraMap = new HashMap<>();

        Collection<Object> eventSyncs = SpringContextUtil.getBeanByType(EventSyncExtra.class);
        for(Object eventSync : eventSyncs){
            if (eventSync instanceof EventSyncExtra){
                extraMap.put(eventSync.getClass().getName(),((EventSyncExtra)eventSync).getExtra());
            }
        }
        event.setExtraData(extraMap);
        if (guavaEvent.async()){
            asyncEventBus.post(event);
        }else{
            eventBus.post(event);
        }

    }

}
