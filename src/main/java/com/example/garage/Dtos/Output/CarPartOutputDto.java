package com.example.garage.Dtos.Output;

import com.example.garage.Models.Car;
import com.example.garage.Models.CarpartName;
import com.example.garage.Models.Repair;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class CarPartOutputDto {

    public CarpartName carpartname;
    public String state;
    public boolean checked;

    //relations.........................................
    @JsonIgnore
    private Car car;
    @JsonIgnore
    List<Repair> repairs;
}
