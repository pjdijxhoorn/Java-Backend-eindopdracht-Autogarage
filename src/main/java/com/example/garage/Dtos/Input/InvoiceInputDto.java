package com.example.garage.Dtos.Input;

import com.example.garage.Models.Car;
import com.example.garage.Models.Service;
import com.example.garage.Models.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class InvoiceInputDto {
    private double totalCost;
    private boolean payed;
    private LocalDate repairDate;
    private User user;

    private Service service;
    private Car car;
}
