package com.absurd.rick.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Created by wangwenwei on 17/6/11.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户")
public class User {
    private String id;

    private String username;

    private String password;

    private String email;

    private Date lastPasswordResetDate;

    private Date date_create;

    private String roles;
}
