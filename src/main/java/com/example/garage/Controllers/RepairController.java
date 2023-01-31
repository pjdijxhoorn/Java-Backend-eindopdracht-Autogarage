package com.example.garage.Controllers;

import com.example.garage.Dtos.Input.RepairInputDto;
import com.example.garage.Dtos.Output.RepairOutputDto;
import com.example.garage.Services.RepairService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.example.garage.Utilities.Utilities.getErrorString;

@RestController
@RequestMapping("/repairs")
public class RepairController {

    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }


    @GetMapping("{licenseplate}")
    public ResponseEntity<Iterable<RepairOutputDto>> getAllRepairsFromOneCar(@PathVariable String licenseplate) {
        return ResponseEntity.ok(repairService.getAllRepairsFromOneCar(licenseplate));
    }

    @PostMapping("/{carpart}/{service_id}")
    public ResponseEntity<String> createRepair(@PathVariable String carpart, @PathVariable long service_id, @Valid @RequestBody RepairInputDto repairInputDto, BindingResult br) {
        if (br.hasErrors()) {
            String errorString = getErrorString(br);
            return new ResponseEntity<>(errorString, HttpStatus.BAD_REQUEST);
        } else {
            long createdId = repairService.createRepair(repairInputDto, carpart, service_id);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/repairs/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Repair Made");
        }
    }

    @PutMapping("{id}/setrepaired")
    public ResponseEntity<RepairOutputDto> SetRepaired(@PathVariable long id, @RequestBody RepairInputDto repairInputDto) {
        return ResponseEntity.ok(repairService.SetRepaired(id, repairInputDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRepair(@PathVariable long id) {
        return ResponseEntity.ok(repairService.deleteRepair(id));
    }

}
