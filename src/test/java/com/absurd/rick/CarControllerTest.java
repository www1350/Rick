package com.absurd.rick;

import com.absurd.rick.model.Car;
import com.absurd.rick.service.CarService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Absurd.
 */
public class CarControllerTest extends BaseTest{
    @Test
    public void test() {
        String carId = "c5aY1btjdx";
        Car product = restTemplate.getForObject("http://localhost:" + port + "/car/" + carId, Car.class);
        assertThat(product.getBrand()).isEqualTo("阿斯顿马丁");

        Car newCar = new Car();
        double newPrice = new Random().nextDouble();
        newCar.setName("new name");
        newCar.setBrand("测试一下");
        newCar.setPrice(newPrice);
        restTemplate.put("http://localhost:" + port + "/car/" + carId, newCar);
    }

    @Autowired
    private CarService carService;

    @Test
    public void test2(){
        String carId = "c5aY1btjdx";
        Car product =carService.get(carId);
        assertThat(product.getBrand()).isEqualTo("阿斯顿马丁");
        Car newCar = new Car();
        double newPrice = new Random().nextDouble();
        newCar.setName("new name");
        newCar.setBrand("测试一下");
        newCar.setPrice(newPrice);
        Car ttCar = carService.update(carId,newCar);
        assertThat(ttCar.getBrand()).isEqualTo("测试一下");
    }



}
