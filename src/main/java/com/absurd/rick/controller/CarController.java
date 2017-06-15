package com.absurd.rick.controller;

import com.absurd.rick.model.Car;
import com.absurd.rick.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @author Absurd.
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/car")
@PreAuthorize("hasRole('ROLE_USER')")
public class CarController {
    @Autowired
    private CarService carService;


    @PostAuthorize("hasAuthority('absurd')")
    @ApiOperation(value="获取车辆", notes="")
    @GetMapping("/{id}")
    public Car getCarInfo(
            @PathVariable("id")
                    String id) {
        Car car =  carService.get(id);

      return car;
    }

    @PutMapping("/{id}")
    public Car updateCarInfo(@PathVariable("id") String id, @RequestBody Car newCar) {
        return carService.update(id,newCar);
    }

    @PostMapping
    public Car saveCarInfo(@RequestBody Car newCar){
        carService.save(newCar);
        return newCar;
    }
}
