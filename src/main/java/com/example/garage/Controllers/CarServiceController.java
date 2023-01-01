package com.example.garage.Controllers;

import com.example.garage.Dtos.Output.CarServiceOutputDto;
import com.example.garage.Services.CarServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Services")
public class CarServiceController {

    private final CarServiceService carServiceService;

    public CarServiceController(CarServiceService carServiceService) {
        this.carServiceService = carServiceService;
    }


    @PostMapping("{car_id}")
    public ResponseEntity<CarServiceOutputDto> createCarService(@PathVariable long car_id){
        return ResponseEntity.ok(carServiceService.createCarService(car_id));
    }
}
