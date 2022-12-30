package com.example.garage.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="invoices")
public class Invoice {
    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    private double totalCost;
    private boolean payed;
    private LocalDate repairDate;
    //list<repairs>repairs;

    private static final double APKCHECK = 30.00;

    //relations.........................................

    @ManyToOne
    @JsonIgnore
    private User user;






}
