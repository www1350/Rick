package com.absurd.rick.event.support.guava;

import com.absurd.rick.event.EventSyncExtra;
import com.absurd.rick.event.Handler;
import com.absurd.rick.event.support.Event;
import com.absurd.rick.util.SpringContextUtil;
import com.google.common.eventbus.AllowConcurrentEvents;

import java.util.Collection;
import java.util.Map;

/**
 * Created by wangwenwei on 2017/8/17.
 */
public class ThreadSafeEventHandlerProxy implements Handler {

    private Handler handler;

    public ThreadSafeEventHandlerProxy(Handler handler) {
        this.handler = handler;
    }


    @AllowConcurrentEvents
    @Override
    public void handler(Event event) {
        resolverExtra(event);
        handler.handler(event);
    }

    private void resolverExtra(Event event) {
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
