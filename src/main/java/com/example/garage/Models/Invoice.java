package com.example.garage.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="invoices")
public class Invoice {
    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    private double totalrepaircost;
    private double totalcost;
    private boolean payed;
    private LocalDate repairDate;


    private static final double btw = 21.0;
    private static final double APKCHECK = 30.00;

    //relations.........................................

    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToOne
    private CarService carService;

    @ManyToOne
    @JsonIgnore
    private Car car;


    public double calculateRepairCost(){
        double total = 0.0;
        for (Repair repair: carService.repairs){
           total+=repair.getRepairCost();
        }
        return total;
   }

   public double calculateTotalCost(){
        double total = 0.0;
        total += APKCHECK;
        total += +totalrepaircost;
        total = total + ((total / 100) * btw);
        total = Math.round(total*100.0)/100.0;
       return  total;
   }
}
