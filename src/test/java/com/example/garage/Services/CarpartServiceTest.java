package com.example.garage.Services;

import com.example.garage.Dtos.Input.CarPartInputDto;
import com.example.garage.Dtos.Output.CarPartOutputDto;

import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.Car;
import com.example.garage.Models.CarPart;
import com.example.garage.Models.CarpartName;

import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.CarpartRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.garage.Models.Carstatus.CHECKED_IN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CarpartServiceTest {

    @Mock
    CarpartRepository carpartRepository;

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarpartService carpartService;

    @Captor
    ArgumentCaptor<CarPart> captor;
    Car car1;
    CarPart tires;
    CarPart brakes;
    CarPartInputDto carPartInputDto1;

    @BeforeEach
    void setUp() {
        car1 = new Car(1L,"33-TTB-3","TOYOTA",CHECKED_IN,null,null,null,null,null);
        tires = new CarPart(1L, CarpartName.TIRES,"3mm profile",true,car1,null);
        brakes = new CarPart(2L, CarpartName.BRAKES,"3mm profile",true,car1,null);

        ArrayList<CarPart> carparts = new ArrayList<>();
        carparts.add(tires);
        carparts.add(brakes);
        car1.setCarparts(carparts);

        carPartInputDto1 = new CarPartInputDto();
        carPartInputDto1.setChecked(false);
        carPartInputDto1.setState("no profile you fool");

    }


    @Test
    void getAllPartsFromCarid() {
        when(carRepository.findBylicenseplate(any())).thenReturn(car1);
        List<CarPartOutputDto> carpartsfound = (List<CarPartOutputDto>) carpartService.getAllPartsFromCarid("33-TTB-3");
        assertEquals(tires.getState(), carpartsfound.get(0).getState());
        assertEquals(tires.getRepairs(), carpartsfound.get(0).getRepairs());
        assertEquals(tires.getCar(), carpartsfound.get(0).getCar());
        assertEquals(brakes.getCarpartname(), carpartsfound.get(1).getCarpartname());

    }
    @Test
    void getAllPartsFromCaridThrowsExceptionForRepairTest() {
        assertThrows(RecordNotFoundException.class, () -> carpartService.getAllPartsFromCarid("33-TTB-3"));

    }


    @Test
    void carpartinspection() {
        when(carRepository.findBylicenseplate(any())).thenReturn(car1);
        when(carpartRepository.findById(any())).thenReturn(Optional.ofNullable(tires));
        when(carpartRepository.save(any())).thenReturn(tires);
        CarPartOutputDto output = carpartService.Carpartinspection("33-TTB-3", "TIRES",carPartInputDto1);

        assertEquals(carPartInputDto1.getState(), output.getState());
        assertEquals(carPartInputDto1.isChecked(), output.isChecked());
    }

    @Test
    void CarpartinspectionThrowsExceptionForRepairTest() {
        assertThrows(RecordNotFoundException.class, () -> carpartService.Carpartinspection("33-TTB-3", "TIRES",carPartInputDto1));

        when(carRepository.findBylicenseplate(any())).thenReturn(car1);
        assertThrows(RecordNotFoundException.class, () -> carpartService.Carpartinspection("33-TTB-3", "TIRES",carPartInputDto1));
    }
}