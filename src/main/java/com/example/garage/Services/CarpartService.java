package com.example.garage.Services;

import com.example.garage.Dtos.Input.CarPartInputDto;
import com.example.garage.Dtos.Output.CarOutputDto;
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


    public  Iterable<CarPartOutputDto> getAllPartsFromCarid(long car_id){
        Optional<Car> optionalcar  = carRepository.findById(car_id);
        if (optionalcar.isEmpty()) {
            throw new RecordNotFoundException("this car seems to be non existing : " + car_id);
        }else{
            ArrayList<CarPartOutputDto> carpartOutputDtos = new ArrayList<>();
            Car car = optionalcar.get();
            for (CarPart a: car.getCarparts()){
                CarPartOutputDto carpartDto = transferCarpartToDto(a);
                carpartOutputDtos.add(carpartDto);
            }
            return carpartOutputDtos;
        }
    }



    public CarPartOutputDto Carpartinspection(long car_id, String carpart, CarPartInputDto carPartinputDto){
        Optional<Car> optionalcar  = carRepository.findById(car_id);
        if (optionalcar.isEmpty()) {
            throw new RecordNotFoundException("this car seems to be non existing : " + car_id);
        }else{
            Car car = optionalcar.get();
            Optional<CarPart> optionalCarPart = Optional.empty();
            CarPart carpart2 = null;
            for (CarPart carpartx: car.getCarparts()){
                String carpartname = String.valueOf(carpartx.getCarpartname());
                if (Objects.equals(carpartname, carpart)){
                    optionalCarPart = carPartRepository.findById(carpartx.getId());
                }
            }
            if (optionalCarPart.isPresent()){
                carpart2 = optionalCarPart.get();
            } else {
                throw new RecordNotFoundException("this car part seems to be non existing : " + carpart);
            }

            carpart2.setState(carPartinputDto.getState());
            carpart2.setChecked(carPartinputDto.isChecked());
            CarPart savedcarpart = carPartRepository.save(carpart2);
            return transferCarpartToDto(savedcarpart);
            }
        }



    private CarPartOutputDto transferCarpartToDto(CarPart carpart){
        CarPartOutputDto carPartOutputDto = new CarPartOutputDto();

        carPartOutputDto.setCar(carpart.getCar());
        carPartOutputDto.setState(carpart.getState());
        carPartOutputDto.setCarpartname(carpart.getCarpartname());
        carPartOutputDto.setChecked(carpart.isChecked());
        carPartOutputDto.setRepairs(carpart.getRepairs());

        return carPartOutputDto;
    }
}
