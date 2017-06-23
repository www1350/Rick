package com.absurd.rick.interceptor;

import com.absurd.rick.config.AuthHolder;
import com.absurd.rick.security.JwtUser;
import com.absurd.rick.util.BeanUtilEx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by wangwenwei on 17/6/19.
 */
@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
         JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            Map<String,Object> map = BeanUtilEx.transBean2Map(jwtUser);
            AuthHolder.set(map);
            log.info("enter {} {}", AuthHolder.username(),AuthHolder.roles());
        }
        return true;
    }
}
