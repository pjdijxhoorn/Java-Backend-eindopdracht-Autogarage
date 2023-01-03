package com.example.garage.Services;

import com.example.garage.Dtos.Output.CarServiceOutputDto;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.Car;
import com.example.garage.Models.CarService;
import com.example.garage.Models.User;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.CarServiceRepository;
import com.example.garage.Repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CarServiceService {

    private final CarRepository carRepository;
    private final CarServiceRepository carServiceRepository;
    private final UserRepository userRepository;


    public CarServiceService(CarRepository carRepository, CarServiceRepository carServiceRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.carServiceRepository = carServiceRepository;
        this.userRepository = userRepository;
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

    public Iterable<CarServiceOutputDto> getAllCarServiceFromUser() {
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            if (currentuser.isPresent()){
                User user = currentuser.get();
                ArrayList<CarServiceOutputDto> carserviceOutputDtos = new ArrayList<>();
                Iterable<Car> allcars = carRepository.findByUser(user);
                for (Car a: allcars){
                    Iterable<CarService> carservices = a.getCarServices();
                    for (CarService b:carservices){
                        CarServiceOutputDto carserviceDto = transferCarServicetoOuputDto(b);
                        carserviceOutputDtos.add(carserviceDto);
                    }
                }
                return carserviceOutputDtos;
            }else {
                throw new RecordNotFoundException("this users seems to have no values");
            }
        }
        throw new RecordNotFoundException("no User is logged in at the moment");
    }

    public CarServiceOutputDto getOneCarServiceByID(long id) {
        Optional<CarService> optionalcarservice = carServiceRepository.findById(id);
        if (optionalcarservice.isEmpty()){
            throw new RecordNotFoundException("no carservice found with id: "+ id);
        }else {
            CarService carservice = optionalcarservice.get();
            return transferCarServicetoOuputDto(carservice);
        }
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

    public String approvalUser(long id, CarServiceOutputDto carServiceOutputDto){
        // get the current user
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<CarService> optionalCarService = carServiceRepository.findById(id);
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            // check if current user is a logged in user and if the carservice exists
            if (currentuser.isEmpty()){
                throw new RecordNotFoundException("this users seems to have no values");
            } else if (optionalCarService.isEmpty()) {
                throw new RecordNotFoundException("couldnt find the carservice with this id: " +id );
            } else {
                System.out.println("1");
                User user = currentuser.get();
                CarService carService = optionalCarService.get();
                // check if the user owns the car from the carservice he is about to approve
                boolean doescarbelong = false;
                for (Car a : user.getCars()){
                    if (a == carService.getCar()) {
                        doescarbelong = true;
                        System.out.println("2");
                        // if car is found break
                        break;
                    }
                }
                // if car is of the user set approve
                if (doescarbelong){
                    System.out.println("3");
                    carService.setRepair_approved(carServiceOutputDto.isRepair_approved());
                    carService.setCustumor_response(true);
                    carServiceRepository.save(carService);
                    if (carService.isRepair_approved()){
                        return "Repair approved";
                    }
                    else{
                        return "Repair is not approved. Service will continu without repairs";
                    }
                }
                else {
                    throw new RecordNotFoundException("you don't own this car so you can not approve the repairs.");
                }
            }
        }
        throw new RecordNotFoundException("no User is logged in at the moment");
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


