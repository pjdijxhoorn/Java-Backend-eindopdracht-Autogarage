package com.example.garage.Dtos.Input;

import com.example.garage.Models.Car;
import com.example.garage.Models.CarpartName;
import com.example.garage.Models.Repair;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CarPartInputDto {
    public String state;
    public boolean checked;
}
