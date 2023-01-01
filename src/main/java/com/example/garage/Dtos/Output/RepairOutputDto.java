package com.example.garage.Dtos.Output;

import com.example.garage.Models.CarPart;
import com.example.garage.Models.CarService;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RepairOutputDto {

    private double repairCost;
    private String notes;
    private CarPart carpart;
    private CarService carService;
    private boolean repair_done;
}
