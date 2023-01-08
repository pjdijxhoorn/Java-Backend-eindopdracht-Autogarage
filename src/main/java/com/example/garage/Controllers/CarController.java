package com.example.garage.Controllers;

import com.example.garage.Dtos.Input.CarInputDto;
import com.example.garage.Dtos.Output.CarOutputDto;
import com.example.garage.Services.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.example.garage.Utilities.Utilities.getErrorString;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<CarOutputDto>> getAllCars() {
        return ResponseEntity.ok(
                carService.getAllCars());
    }

    @GetMapping("{id}")
    public ResponseEntity<CarOutputDto> getOneCarByID(@PathVariable long id) {
        return ResponseEntity.ok(carService.getOneCarByID(id));
    }

    @GetMapping("/licenseplate/{licenseplate}")
    public ResponseEntity<CarOutputDto> getOneCarByLincensePlate(@PathVariable String licenseplate) {
        return ResponseEntity.ok(carService.getOneCarByLincensePlate(licenseplate));
    }

    @GetMapping("/user")
    public ResponseEntity<Iterable<CarOutputDto>> getAllCarsfromUser() {
        return ResponseEntity.ok(carService.getAllCarsfromUser());
    }

    @GetMapping("/user/status")
    public ResponseEntity<String> getAllCarsStatusfromUser() {
        return ResponseEntity.ok(carService.getAllCarsStatusfromUser());
    }

    @PostMapping("")
    public ResponseEntity<String> createCar(@Valid @RequestBody CarInputDto carInputDto, BindingResult br) {
        if (br.hasErrors()) {
            String errorString = getErrorString(br);
            return new ResponseEntity<>(errorString, HttpStatus.BAD_REQUEST);

        } else {
            long createdId = carService.createCar(carInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/car/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Car created");
        }
    }

    @PutMapping("/statusdesk/{carstatus}/{licenseplate}")
    public ResponseEntity<CarOutputDto> updateCarStatusDesk(@PathVariable String licenseplate, @PathVariable String carstatus) {
        return ResponseEntity.ok(carService.updateCarStatusDesk(licenseplate, carstatus));
    }

    @PutMapping("statusmechanic/{carstatus}/{licenseplate}")
    public ResponseEntity<CarOutputDto> updateCarStatusMechanic(@PathVariable String licenseplate, @PathVariable String carstatus) {
        return ResponseEntity.ok(carService.updateCarStatusMechanic(licenseplate, carstatus));
    }

    @PutMapping("{id}")
    public ResponseEntity<CarOutputDto> updateCar(@PathVariable long id, @RequestBody CarOutputDto carOutputDto) {
        return ResponseEntity.ok(carService.updateCar(id, carOutputDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCar(@PathVariable long id) {
        return ResponseEntity.ok(carService.deleteCar(id));
    }
}
