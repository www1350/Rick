package com.absurd.rick.service.impl;

import com.absurd.rick.annotation.GuavaEvent;
import com.absurd.rick.mapper.UserMapper;
import com.absurd.rick.model.User;
import com.absurd.rick.security.JwtUser;
import com.absurd.rick.service.AuthService;
import com.absurd.rick.util.JwtTokenUtil;
import com.absurd.rick.param.UserRegisterParam;
import com.absurd.rick.util.UUIDUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by wangwenwei on 17/6/13.
 */
@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserMapper userMapper;


    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
    }

    @Override
    public User register(UserRegisterParam userToAdd) {
        final String username = userToAdd.getUsername();
        if(userMapper.getByUserName(username)!=null) {
            throw new RuntimeException("已存在");
        }
        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        User user = new User();
        BeanUtils.copyProperties(userToAdd,user);
        user.setPassword(encoder.encode(rawPassword));
        user.setId(UUIDUtil.getID());
        userMapper.save(user);
        return user;
    }

    @GuavaEvent(value = "auth.login")
    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken;
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
