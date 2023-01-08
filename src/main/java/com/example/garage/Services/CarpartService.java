package com.example.garage.Services;

import com.example.garage.Dtos.Input.CarPartInputDto;
import com.example.garage.Dtos.Output.CarPartOutputDto;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.Car;
import com.example.garage.Models.CarPart;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.CarpartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarpartService {

    private final CarRepository carRepository;
    private final CarpartRepository carPartRepository;

    public CarpartService(CarRepository carRepository, CarpartRepository carPartRepository) {
        this.carRepository = carRepository;
        this.carPartRepository = carPartRepository;
    }

    public Iterable<CarPartOutputDto> getAllPartsFromCarid(String licenseplate) {
        Car car = carRepository.findBylicenseplate(licenseplate);
        if (car == null) {
            throw new RecordNotFoundException("no car found with license-plate: " + licenseplate);
        }
        ArrayList<CarPartOutputDto> carpartOutputDtos = new ArrayList<>();
        for (CarPart a : car.getCarparts()) {
            CarPartOutputDto carpartDto = transferCarpartToDto(a);
            carpartOutputDtos.add(carpartDto);
        }
        return carpartOutputDtos;
    }


    public CarPartOutputDto Carpartinspection(String licenseplate, String carpart, CarPartInputDto carPartinputDto) {
        Car car = carRepository.findBylicenseplate(licenseplate);
        if (car == null) {
            throw new RecordNotFoundException("no car found with license-plate: " + licenseplate);
        }
        Optional<CarPart> optionalCarPart = Optional.empty();
        CarPart carpart2 = null;
        for (CarPart carpartx : car.getCarparts()) {
            String carpartname = String.valueOf(carpartx.getCarpartname());
            if (Objects.equals(carpartname, carpart)) {
                optionalCarPart = carPartRepository.findById(carpartx.getId());
            }
        }
        if (optionalCarPart.isPresent()) {
            carpart2 = optionalCarPart.get();
        } else {
            throw new RecordNotFoundException("this car part seems to be non existing : " + carpart);
        }

        carpart2.setState(carPartinputDto.getState());
        carpart2.setChecked(carPartinputDto.isChecked());
        CarPart savedcarpart = carPartRepository.save(carpart2);
        return transferCarpartToDto(savedcarpart);
    }


    private CarPartOutputDto transferCarpartToDto(CarPart carpart) {
        CarPartOutputDto carPartOutputDto = new CarPartOutputDto();


        carPartOutputDto.setState(carpart.getState());
        carPartOutputDto.setCarpartname(carpart.getCarpartname());
        carPartOutputDto.setChecked(carpart.isChecked());
        if (carPartOutputDto.getRepairs() == null) {
            carPartOutputDto.setRepairs(carpart.getRepairs());
        }
        if (carPartOutputDto.getCar() == null) {
            carPartOutputDto.setCar(carpart.getCar());
        }

        return carPartOutputDto;
    }
}
