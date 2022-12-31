package com.example.garage.Dtos.Input;

import com.example.garage.Models.CarPart;
import com.example.garage.Models.Service;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RepairInputDto {


    private double repairCost;
    private String notes;
    private CarPart carpart;
    private Service service;
}
