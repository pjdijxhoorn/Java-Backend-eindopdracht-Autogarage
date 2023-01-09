package com.example.garage.Controllers;

import com.example.garage.Dtos.Output.MaintenanceOutputDto;
import com.example.garage.Services.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<MaintenanceOutputDto>> getAllCarServices() {
        return ResponseEntity.ok(maintenanceService.getAllCarServices());
    }

    @GetMapping("{id}")
    public ResponseEntity<MaintenanceOutputDto> getOneCarServiceByID(@PathVariable long id) {
        return ResponseEntity.ok(maintenanceService.getOneCarServiceByID(id));
    }

    @GetMapping("/user")
    public ResponseEntity<Iterable<MaintenanceOutputDto>> getAllCarServiceFromUser() {
        return ResponseEntity.ok(maintenanceService.getAllCarServiceFromUser());
    }

    @PostMapping("{car_id}")
    public ResponseEntity<MaintenanceOutputDto> createCarService(@PathVariable long car_id) {
        return ResponseEntity.ok(maintenanceService.createCarService(car_id));
    }

    @PutMapping("{id}/mechanicdone")
    public ResponseEntity<MaintenanceOutputDto> mechanicIsDone(@PathVariable long id, @RequestBody MaintenanceOutputDto maintenanceOutputDto) {
        return ResponseEntity.ok(maintenanceService.mechanicIsDone(id, maintenanceOutputDto));
    }

    @PutMapping("{id}/approvaluser")
    public ResponseEntity<String> approvalUser(@PathVariable long id, @RequestBody MaintenanceOutputDto maintenanceOutputDto) {
        return ResponseEntity.ok(maintenanceService.approvalUser(id, maintenanceOutputDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletecarservice(@PathVariable long id) {
        return ResponseEntity.ok(maintenanceService.deletecarservice(id));
    }

}
