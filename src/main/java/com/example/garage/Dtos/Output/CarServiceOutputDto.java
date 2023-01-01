package com.example.garage.Dtos.Output;

import com.example.garage.Models.Car;
import com.example.garage.Models.Invoice;
import com.example.garage.Models.Repair;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class CarServiceOutputDto {

    private boolean repair_approved;
    private boolean custumor_response;
    private  boolean mechanic_done;
    //relations.........................................

    List<Repair> repairs;
    private Car car;
    private Invoice invoice;
}
