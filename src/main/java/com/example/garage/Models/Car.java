package com.example.garage.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cars")
public class Car {
    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;
    @NotNull(message = "This isnt allowed to be Null " )
    private String licenseplate;
    private String brand;
    @Enumerated(EnumType.STRING)
    private Carstatus carstatus;

    //relations.........................................
    @OneToMany(mappedBy = "car")
    List<CarPart> carparts;

    @ManyToOne
    @JsonIgnore
    private User user;
}
