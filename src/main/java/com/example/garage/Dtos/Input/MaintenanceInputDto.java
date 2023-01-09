package com.example.garage.Dtos.Input;

import com.example.garage.Models.Car;
import com.example.garage.Models.Invoice;
import com.example.garage.Models.Repair;

import java.util.List;

public class MaintenanceInputDto {
    //variables.........................................
    private boolean repair_approved;
    private boolean custumor_response;
    private boolean mechanic_done;
    private boolean totalrepaircost;
    //relations.........................................

    List<Repair> repairs;
    private Car car;
    private Invoice invoice;
}
