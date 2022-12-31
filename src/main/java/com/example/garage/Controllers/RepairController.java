package com.example.garage.Controllers;

import com.example.garage.Dtos.Input.RepairInputDto;
import com.example.garage.Services.RepairService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("")
    public ResponseEntity <String> createRepair(@Valid @RequestBody RepairInputDto repairInputDto, BindingResult br){
        if (br.hasErrors()) {
            String errorString = getErrorString(br);
            return new ResponseEntity<>(errorString, HttpStatus.BAD_REQUEST);
        } else {
            long createdId = repairService.createRepair(repairInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/repairs/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Repair Made");
    }

}}
