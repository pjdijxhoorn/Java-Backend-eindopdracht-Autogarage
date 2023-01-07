package com.example.garage.Controllers;

import com.example.garage.Dtos.Input.CarPartInputDto;
import com.example.garage.Dtos.Output.CarPartOutputDto;
import com.example.garage.Services.CarpartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carparts")
public class CarpartController {

    private final CarpartService carpartService;

    public CarpartController(CarpartService carpartService) {
        this.carpartService = carpartService;
    }

    @GetMapping("{car_id}")
    public ResponseEntity<Iterable<CarPartOutputDto>> getAllPartsFromCarid(@PathVariable long car_id) {
        return ResponseEntity.ok(carpartService.getAllPartsFromCarid(car_id));
    }


    @PutMapping("{car_id}/inspection/{carpart}")
    public ResponseEntity<CarPartOutputDto> Carpartinspection(@PathVariable long car_id, @PathVariable String carpart, @RequestBody CarPartInputDto carPartinputDto) {
        return ResponseEntity.ok(carpartService.Carpartinspection(car_id, carpart, carPartinputDto));
    }


}
