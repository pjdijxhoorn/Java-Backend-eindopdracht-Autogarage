package com.example.garage.Dtos.Output;

import com.example.garage.Models.Car;
import com.example.garage.Models.CarService;
import com.example.garage.Models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class InvoiceOutputDto {
    private double totalRepairCost;
    private double totalCost;
    private boolean payed;
    private LocalDate repairDate;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private CarService carService;
    @JsonIgnore
    private Car car;
}
