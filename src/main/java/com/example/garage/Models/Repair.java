package com.example.garage.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repairs")
public class Repair {
    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    private double repairCost;
    private String notes;
    @Value("${some.key:false}")
    private boolean repair_done;

    //relations.........................................

    @ManyToOne
    @JsonIgnore
    private CarPart carpart;

    @ManyToOne
    @JsonIgnore
    private Maintenance maintenance;
}
