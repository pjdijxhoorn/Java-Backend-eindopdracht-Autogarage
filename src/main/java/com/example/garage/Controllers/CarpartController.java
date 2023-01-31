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

    @GetMapping("{licenseplate}")
    public ResponseEntity<Iterable<CarPartOutputDto>> getAllPartsFromCarid(@PathVariable String licenseplate) {
        return ResponseEntity.ok(carpartService.getAllPartsFromCarid(licenseplate));
    }


    @PutMapping("{licenseplate}/inspection/{carpart}")
    public ResponseEntity<CarPartOutputDto> Carpartinspection(@PathVariable String licenseplate, @PathVariable String carpart, @RequestBody CarPartInputDto carPartinputDto) {
        return ResponseEntity.ok(carpartService.Carpartinspection(licenseplate, carpart, carPartinputDto));
    }


}
