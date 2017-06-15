package com.absurd.rick.param;

import lombok.Data;

/**
 * Created by wangwenwei on 17/6/14.
 */
@Data
public class UserRegisterParam {
    private String username;

    private String password;

    private String email;

    private String roles;
}
