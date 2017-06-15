package com.absurd.rick.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by wangwenwei on 17/6/14.
 */
public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
