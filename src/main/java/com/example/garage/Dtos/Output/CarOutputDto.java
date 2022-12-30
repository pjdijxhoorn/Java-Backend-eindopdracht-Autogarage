package com.example.garage.Dtos.Output;

import com.example.garage.Models.CarPart;
import com.example.garage.Models.Carstatus;
import com.example.garage.Models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Setter
@Getter
public class CarOutputDto {

    private String licenseplate;
    private String brand;
    @Enumerated(EnumType.STRING)
    private Carstatus carstatus;
    List<CarPart> carparts;

    @JsonIgnoreProperties(value = { "password","apikey", "authorities", "cars"})
    private User user;
}
