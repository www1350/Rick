package com.absurd.rick;

import com.absurd.rick.model.User;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wangwenwei on 17/8/1.
 */
public class UserMvcTest extends SpringMvcTest{
    @Test
    public void getById(){
        String carId = "03gbrE3l7I";
        User user = restTemplate.getForObject("http://localhost:" + port + "/user/" + carId, User.class);
        assertThat(user.getEmail()).isEqualTo("w@absurd.com");
    }
}
