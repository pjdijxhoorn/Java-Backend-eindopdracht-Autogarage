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
@Table(name = "carparts")
public class CarPart {
    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    public Long id;
    @Enumerated(EnumType.STRING)
    public CarpartName carpartname;
    public String state;
    public boolean checked;

    //relations.........................................
    @ManyToOne
    @JsonIgnore
    private Car car;

    @OneToMany(mappedBy = "carpart")
    List<Repair> repairs;

    public CarPart(CarpartName carpartname, String state, boolean checked) {
        this.carpartname = carpartname;
        this.state = state;
        this.checked = checked;
    }
}

