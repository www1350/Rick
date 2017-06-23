package com.absurd.rick.event;

import com.absurd.rick.config.AuthHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Component
public class AuthEventSyncExtra  implements EventSyncExtra{
    @Override
    public Object getExtra() {
        return AuthHolder.getThreadMap();
    }

    @Override
    public void setExtra(Object extra) {
        AuthHolder.set((Map<String, Object>)extra);

    }
}
