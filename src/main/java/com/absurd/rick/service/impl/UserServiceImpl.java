package com.absurd.rick.service.impl;

import com.absurd.rick.annotation.SpringEvent;
import com.absurd.rick.mapper.UserMapper;
import com.absurd.rick.model.User;
import com.absurd.rick.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangwenwei on 17/8/1.
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @SpringEvent(value = "user.info")
    @Override
    public User getUserInfo(String uid) {
        return userMapper.get(uid);
    }
}
