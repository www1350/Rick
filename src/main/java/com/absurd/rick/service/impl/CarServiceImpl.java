package com.absurd.rick.service.impl;

import com.absurd.rick.annotation.GuavaEvent;
import com.absurd.rick.lock.RedisLock;
import com.absurd.rick.lock.ZookeeperLock;
import com.absurd.rick.mapper.CarMapper;
import com.absurd.rick.util.BeanUtilEx;
import com.absurd.rick.annotation.Cache;
import com.absurd.rick.model.Car;
import com.absurd.rick.service.CarService;
import com.absurd.rick.util.UUIDUtil;
import com.absurd.rick.zookeeper.ZooKeeperOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * @author Absurd.
 */
@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private ZooKeeperOperator zooKeeperOperator;

    @Autowired
    private RedisTemplate redisTemplate;

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

    @Override
    public boolean priceUpdate(String id, Double increPrice) {
//        RedisLock lock = new RedisLock(redisTemplate,"priceUpdate");
        ZookeeperLock lock = new ZookeeperLock(zooKeeperOperator,"priceUpdate");
        lock.lock();
        try {
            Car car = carMapper.get(id);
            Double price = car.getPrice() + increPrice;
            car.setPrice(price);
            carMapper.update(car);
        }finally {
            lock.unlock();
        }
        return true;
    }


    @GuavaEvent(value = "car.get")
    @Cache(time = 30,key = "car-info-get")
    @Override
    public Car get(String id) {
        if (StringUtils.isEmpty(id)) return null;
        return carMapper.get(id);
    }
}
