package com.absurd.rick.mapper;

import com.absurd.rick.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by wangwenwei on 17/6/11.
 */
@Mapper
public interface UserMapper {
    User get(String id);

    User getByUserName(@Param("username") String username);

    void save(User userToAdd);
}
