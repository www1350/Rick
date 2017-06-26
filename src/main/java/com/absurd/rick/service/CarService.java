package com.absurd.rick.service;

import com.absurd.rick.model.Car;


/**
 * @author Absurd.
 */
public interface CarService {
    Car save(Car car);

    Car update(String id, Car newCar);

    boolean priceUpdate(String id,Double increPrice);

    Car get(String id);
}
