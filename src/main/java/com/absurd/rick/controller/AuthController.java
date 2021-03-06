package com.absurd.rick.controller;

import com.absurd.rick.model.User;
import com.absurd.rick.param.UserAuthParam;
import com.absurd.rick.param.UserRegisterParam;
import com.absurd.rick.service.AuthService;
import com.absurd.rick.view.AuthView;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangwenwei on 17/6/13.
 */
@Api
@RestController
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserAuthParam userAuthParam) throws AuthenticationException {
        final String token = authService.login(userAuthParam.getUsername(), userAuthParam.getPassword());

        // Return the token
        return ResponseEntity.ok(new AuthView(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, @RequestParam(required = false) String token) throws AuthenticationException{
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader(tokenHeader).substring(tokenHead.length());
        }
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new AuthView(refreshedToken));
        }
    }

    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
    public User register(@RequestBody UserRegisterParam addedUser) throws AuthenticationException{
        return authService.register(addedUser);
    }
}
