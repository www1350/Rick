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

    int updatePriceWithVersion(@Param("price") double addPrice,@Param("id")String id,
                                @Param("oldVersion") int oldVersion,@Param("version")int version);

    void save(Car car);
}
