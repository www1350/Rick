package com.absurd.rick.service.impl;

import com.absurd.rick.mapper.CarMapper;
import com.absurd.rick.util.BeanUtilEx;
import com.absurd.rick.annotation.Cache;
import com.absurd.rick.model.Car;
import com.absurd.rick.service.CarService;
import com.absurd.rick.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * @author Absurd.
 */
@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarMapper carMapper;
    @Override public Car save(Car car) {
        car.setId(UUIDUtil.getID());
         carMapper.save(car);

        return car;
    }


    @Override
    public Car update(String id, Car newCar) {
        Car car = carMapper.get(id);
        BeanUtilEx.copyPropertiesIgnoreNull(newCar,car,"id");
        carMapper.update(car);
        return car;

    }


    @Cache(time = 30,key = "car-info-get")
    @Override
    public Car get(String id) {
        if (StringUtils.isEmpty(id)) return null;
        return carMapper.get(id);
    }
}
