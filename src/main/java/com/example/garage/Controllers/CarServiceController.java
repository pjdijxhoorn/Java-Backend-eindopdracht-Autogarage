package com.example.garage.Controllers;

import com.example.garage.Dtos.Output.CarServiceOutputDto;
import com.example.garage.Services.CarServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
public class CarServiceController {

    private final CarServiceService carServiceService;

    public CarServiceController(CarServiceService carServiceService) {
        this.carServiceService = carServiceService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<CarServiceOutputDto>> getAllCarServices() {
        return ResponseEntity.ok(carServiceService.getAllCarServices());
    }

    @GetMapping("{id}")
    public ResponseEntity<CarServiceOutputDto> getOneCarServiceByID(@PathVariable long id) {
        return ResponseEntity.ok(carServiceService.getOneCarServiceByID(id));
    }

    @GetMapping("/user")
    public ResponseEntity<Iterable<CarServiceOutputDto>> getAllCarServiceFromUser() {
        return ResponseEntity.ok(carServiceService.getAllCarServiceFromUser());
    }

    @PostMapping("{car_id}")
    public ResponseEntity<CarServiceOutputDto> createCarService(@PathVariable long car_id) {
        return ResponseEntity.ok(carServiceService.createCarService(car_id));
    }

    @PutMapping("{id}/mechanicdone")
    public ResponseEntity<CarServiceOutputDto> mechanicIsDone(@PathVariable long id, @RequestBody CarServiceOutputDto carServiceOutputDto) {
        return ResponseEntity.ok(carServiceService.mechanicIsDone(id, carServiceOutputDto));
    }

    @PutMapping("{id}/approvaluser")
    public ResponseEntity<String> approvalUser(@PathVariable long id, @RequestBody CarServiceOutputDto carServiceOutputDto) {
        return ResponseEntity.ok(carServiceService.approvalUser(id, carServiceOutputDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletecarservice(@PathVariable long id) {
        return ResponseEntity.ok(carServiceService.deletecarservice(id));
    }

}
