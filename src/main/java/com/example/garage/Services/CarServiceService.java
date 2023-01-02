package com.example.garage.Services;

import com.example.garage.Dtos.Output.CarOutputDto;
import com.example.garage.Dtos.Output.CarServiceOutputDto;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.Car;
import com.example.garage.Models.CarService;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.CarServiceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CarServiceService {

    private final CarRepository carRepository;
    private final CarServiceRepository carServiceRepository;

    public CarServiceService(CarRepository carRepository, CarServiceRepository carServiceRepository) {
        this.carRepository = carRepository;
        this.carServiceRepository = carServiceRepository;
    }

    public Iterable<CarServiceOutputDto> getAllCarServices() {
        ArrayList<CarServiceOutputDto> carServiceOutputDtos = new ArrayList<>();
        Iterable<CarService> allcarservices = carServiceRepository.findAll();
        for (CarService a: allcarservices){
            CarServiceOutputDto carserviceDto = transferCarServicetoOuputDto(a);
            carServiceOutputDtos.add(carserviceDto);
        }
        return carServiceOutputDtos;
    }





    public CarServiceOutputDto createCarService(long car_id) {
        Optional<Car> optionalcar = carRepository.findById(car_id);
        if (optionalcar.isEmpty()) {
            throw new RecordNotFoundException("No car found with the id of : " + car_id);
        } else {
            Car car = optionalcar.get();
            CarService newcarservice = new CarService();
            newcarservice.setCar(car);
            newcarservice.setRepair_approved(false);
            newcarservice.setCustumor_response(false);
            newcarservice.setMechanic_done(false);
            newcarservice.setTotalrepaircost(newcarservice.calculateRepairCost());

            CarService savedcarservice = carServiceRepository.save(newcarservice);

            return transferCarServicetoOuputDto(savedcarservice);
        }
    }

    public CarServiceOutputDto mechanicIsDone(long id, CarServiceOutputDto carServiceOutputDto){
        Optional<CarService> optionalCarService = carServiceRepository.findById(id);
        if (optionalCarService.isEmpty()){
            throw new RecordNotFoundException("couldnt find the carservice with this id: " +id );
        }else{
            CarService carService = optionalCarService.get();
            carService.setMechanic_done(carServiceOutputDto.isMechanic_done());
            carServiceRepository.save(carService);
            return transferCarServicetoOuputDto(carService);
        }


    }

    private CarServiceOutputDto transferCarServicetoOuputDto(CarService carService) {
        CarServiceOutputDto carServiceOutputDto = new CarServiceOutputDto();
        carServiceOutputDto.setCar(carService.getCar());
        carServiceOutputDto.setRepairs(carService.getRepairs());
        carServiceOutputDto.setInvoice(carService.getInvoice());

        carServiceOutputDto.setRepair_approved(carService.isRepair_approved());
        carServiceOutputDto.setCustumor_response(carService.isCustumor_response());
        carServiceOutputDto.setMechanic_done(carService.isMechanic_done());
        carServiceOutputDto.setTotalrepaircost(carServiceOutputDto.calculateRepairCost());
        return carServiceOutputDto;
    }
}


