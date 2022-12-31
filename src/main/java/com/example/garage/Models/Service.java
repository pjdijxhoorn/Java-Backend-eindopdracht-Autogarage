package com.example.garage.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="services")
public class Service {
    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    //relations.........................................
    @OneToMany(mappedBy = "carpart")
    List<Repair> repairs;

    @ManyToOne
    @JsonIgnore
    private Car car;

    @OneToOne(mappedBy = "service")
    private Invoice invoice;

}
