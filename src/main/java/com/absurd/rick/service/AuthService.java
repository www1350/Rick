package com.absurd.rick.service;

import com.absurd.rick.annotation.GuavaEvent;
import com.absurd.rick.model.User;
import com.absurd.rick.param.UserRegisterParam;

/**
 * Created by wangwenwei on 17/6/13.
 */
public interface AuthService {

    User register(UserRegisterParam userToAdd);
    @GuavaEvent(value = "auth.login")
    String login(String username, String password);
    String refresh(String oldToken);
}
