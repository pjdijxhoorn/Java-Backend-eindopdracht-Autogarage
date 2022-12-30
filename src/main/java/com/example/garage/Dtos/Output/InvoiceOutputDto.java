package com.example.garage.Dtos.Output;

import com.example.garage.Models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
public class InvoiceOutputDto {
    private double totalCost;
    private boolean payed;
    private LocalDate repairDate;
    @JsonIgnoreProperties(value = { "password","apikey", "authorities", "cars", "enabled","invoices"})
    private User user;
}
