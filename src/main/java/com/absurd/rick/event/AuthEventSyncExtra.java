package com.absurd.rick.event;

import com.absurd.rick.config.AuthHolder;
import org.springframework.stereotype.Component;

/**
 * Created by wangwenwei on 17/6/23.
 */
@Component
public class AuthEventSyncExtra  implements EventSyncExtra{
    @Override
    public Object getExtra() {
        return AuthHolder.getThreadMap();
    }
}
