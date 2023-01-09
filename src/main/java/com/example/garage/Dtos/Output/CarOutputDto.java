package com.example.garage.Dtos.Output;

import com.example.garage.Models.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private User user;
    @JsonIgnore
    private CarPaper carpaper;
    @JsonIgnoreProperties(value = {"pdfinvoice"})
    private Invoice invoice;
}
