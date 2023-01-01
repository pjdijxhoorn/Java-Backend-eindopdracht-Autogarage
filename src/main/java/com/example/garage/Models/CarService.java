package com.example.garage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="carservices")
public class CarService {
    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    @Value("${some.key:false}")
    private boolean repair_approved;
    @Value("${some.key:false}")
    private boolean custumor_response;
    @Value("${some.key:false}")
    private  boolean mechanic_done;
    private double totalrepaircost;
    //relations.........................................
    @OneToMany(mappedBy = "carpart")
    List<Repair> repairs;

    @ManyToOne
    @JsonIgnore
    private Car car;

    @OneToOne(mappedBy = "carService")
    private Invoice invoice;

    public double calculateRepairCost(){
        double total = 0.0;
        //total repair price is the combined repair items
        if (repairs != null){
            for (Repair repair: repairs){
                total += repair.getRepairCost();}
        }else{
            total= 0.0;}
        return total;
    }
}
