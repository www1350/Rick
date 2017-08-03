package com.absurd.rick.event.support.spring;

import com.absurd.rick.event.support.Event;
import org.springframework.context.ApplicationEvent;

/**
 * Created by wangwenwei on 17/8/2.
 */
public class SpringAppEvent extends ApplicationEvent {

    private Event event;

    public SpringAppEvent(Object source, Event event) {
        super(source);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
