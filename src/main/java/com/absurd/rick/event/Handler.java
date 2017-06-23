package com.absurd.rick.event;

import com.absurd.rick.util.SpringContextUtil;
import com.google.common.eventbus.Subscribe;

import java.util.Collection;
import java.util.Map;

/**
 * Created by wangwenwei on 17/6/23.
 */
public interface Handler {
    default void initExtra(Event event){
        Map<String,Object> map =  event.getExtraData();
        Collection<Object> eventSyncs = SpringContextUtil.getBeanByType(EventSyncExtra.class);
        for(Object eventSync : eventSyncs){
            if (eventSync instanceof EventSyncExtra){
                EventSyncExtra eventSyncExtra =  (EventSyncExtra) eventSync;
                eventSyncExtra.setExtra(map.get(eventSync.getClass().getName()));
            }
        }
    }

    @Subscribe
    void handler(Event event);


}
