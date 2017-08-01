package com.absurd.rick.controller;

import com.absurd.rick.model.User;
import com.absurd.rick.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangwenwei on 17/8/1.
 */
@Api
@RequestMapping(value = "/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public User getUserInfo(@PathVariable String uid){
        return userService.getUserInfo(uid);
    }
}
