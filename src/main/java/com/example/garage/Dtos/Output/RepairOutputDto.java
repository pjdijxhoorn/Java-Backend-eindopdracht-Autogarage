package com.example.garage.Dtos.Output;

import com.example.garage.Models.CarPart;
import com.example.garage.Models.CarService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RepairOutputDto {

    private double repairCost;
    private String notes;
    @JsonIgnore
    private CarPart carpart;
    @JsonIgnore
    private CarService carService;
    private boolean repair_done;
}
