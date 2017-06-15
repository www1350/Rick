package com.absurd.rick.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * Created by wangwenwei on 17/6/11.
 */
@Data
@ApiModel("用户")
public class User {
    private String id;

    private String username;

    private String password;

    private String email;

    private Date lastPasswordResetDate;

    private Date dateCreate;

    private String roles;
}
