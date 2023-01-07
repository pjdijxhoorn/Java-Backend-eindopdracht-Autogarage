package com.example.garage.Dtos.Output;

import com.example.garage.Models.CarPart;
import com.example.garage.Models.CarService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RepairOutputDto {

    private double repairCost;
    private String notes;
    @JsonIgnoreProperties(value = {"repairs"})
    private CarPart carpart;
    @JsonIgnoreProperties(value = {"repairs"})
    private CarService carService;
    private boolean repair_done;
}
