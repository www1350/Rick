package com.absurd.rick.security;

import com.absurd.rick.model.User;
import com.google.common.base.Splitter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wangwenwei on 17/6/11.
 */
public final class JwtUserFactory {

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                mapToGrantedAuthorities(user.getRoles()),
                user.getLastPasswordResetDate()
        );
    }

    private JwtUserFactory() {
    }

    public static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
//        Splitter splitter =  Splitter.on(",").trimResults();
//        return splitter.splitToList(authorities).stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
