package com.example.garage.Dtos.Output;

import com.example.garage.Models.Car;
import com.example.garage.Models.Invoice;
import com.example.garage.Models.Repair;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CarServiceOutputDto {
    //variables.........................................
    private boolean repair_approved;
    private boolean custumor_response;
    private boolean mechanic_done;
    private double totalrepaircost;
    //relations.........................................

    List<Repair> repairs;
    @JsonIgnoreProperties(value = {"carparts", "invoices", "carServices", "invoices"})
    private Car car;
    private Invoice invoice;

    public double calculateRepairCost() {
        double total = 0.0;
        //total repair price is the combined repair items
        if (repairs != null) {
            for (Repair repair : repairs) {
                total += repair.getRepairCost();
            }
        } else {
            total = 0.0;
        }
        return total;
    }
}
