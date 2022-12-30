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
@Table(name="carparts")
public class CarPart {
    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    public Long id;
    @Enumerated(EnumType.STRING)
    public Name name;
    public String state;
    public boolean checked;

    //relations.........................................
    @ManyToOne
    @JsonIgnore
    private Car car;

    public CarPart(Name name, String state, boolean checked) {
        this.name = name;
        this.state = state;
        this.checked = checked;
    }

}
