package com.absurd.rick;

import com.absurd.rick.annotation.BeanAlias;
import com.absurd.rick.param.UserRegisterParam;
import lombok.Data;

import java.util.Date;

/**
 * Created by wangwenwei on 17/6/16.
 */
@Data
public class UserRegParam extends UserRegisterParam{

    @BeanAlias(value = "date_create")
    private Date dateCreate;
}
