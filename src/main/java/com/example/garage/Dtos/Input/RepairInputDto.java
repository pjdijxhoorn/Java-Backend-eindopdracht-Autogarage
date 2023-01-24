package com.example.garage.Dtos.Input;

import com.example.garage.Models.CarPart;
import com.example.garage.Models.Maintenance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepairInputDto {

    private double repairCost;
    private String notes;
    private CarPart carpart;
    private Maintenance maintenance;
    private boolean repair_done;
}
