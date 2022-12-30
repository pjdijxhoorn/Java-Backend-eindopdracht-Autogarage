package com.example.garage.Dtos.Input;

import com.example.garage.Models.CarPart;
import com.example.garage.Models.Carstatus;
import com.example.garage.Models.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Setter
@Getter
public class CarInputDto {

    private String licenseplate;
    private String brand;
    @Enumerated(EnumType.STRING)
    private Carstatus carstatus;
    List<CarPart> carparts;
    private User user;

}
