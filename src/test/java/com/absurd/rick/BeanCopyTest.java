package com.absurd.rick;

import com.absurd.rick.model.User;
import com.absurd.rick.util.BeanUtilEx;
import com.absurd.rick.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.Date;

/**
 * Created by wangwenwei on 17/6/16.
 */
@Slf4j
public class BeanCopyTest {
    @Test
    public void copyBean(){
        UserRegParam param = new UserRegParam();
        param.setUsername("aaa");
        param.setEmail("ww@ww.com");
        param.setRoles("addd");
        param.setDateCreate(new Date());
        User user = new User();
        user.setId(UUIDUtil.getID());
        user.setPassword("dfsa");
        BeanUtilEx.copyPropertiesIgnoreNull(param,user,"roles");
//        BeanUtils.copyProperties(param,user,"roles");
        log.info(user+"");

    }
}
