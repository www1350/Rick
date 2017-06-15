package com.absurd.rick.mapper;

import com.absurd.rick.model.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author Absurd.
 */
@Mapper
public interface CarMapper {
//    @Select("SELECT * FROM car WHERE id = #{id}")
    Car get(@Param("id")String id);

    void update(Car car);

    void save(Car car);
}
