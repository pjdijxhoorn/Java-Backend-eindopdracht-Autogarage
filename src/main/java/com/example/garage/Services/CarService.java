package com.example.garage.Services;

import com.example.garage.Dtos.Input.CarInputDto;
import com.example.garage.Dtos.Output.CarOutputDto;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.*;
import com.example.garage.Repositories.CarPaperRepository;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.CarpartRepository;
import com.example.garage.Repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.garage.Utilities.licenseplateValidator.validateLicensePlate;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarpartRepository carpartRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CarPaperRepository carPaperRepository;

    public CarService(CarRepository carRepository, CarpartRepository carpartRepository, UserRepository userRepository, EmailService emailService, CarPaperRepository carPaperRepository) {
        this.carRepository = carRepository;
        this.carpartRepository = carpartRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.carPaperRepository = carPaperRepository;
    }

    public Iterable<CarOutputDto> getAllCars() {
        ArrayList<CarOutputDto> carOutputDtos = new ArrayList<>();
        Iterable<Car> allcars = carRepository.findAll();
        for (Car a : allcars) {
            CarOutputDto AutoDto = transferCarToDto(a);
            carOutputDtos.add(AutoDto);
        }

        return carOutputDtos;
    }

    public CarOutputDto getOneCarByID(long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()) {
            throw new RecordNotFoundException("no car found with id: " + id);
        } else {
            Car car1 = car.get();
            return transferCarToDto(car1);
        }
    }

    public CarOutputDto getOneCarByLincensePlate(String licenseplate) {
        Car car = carRepository.findBylicenseplate(licenseplate);
        if (car == null) {
            throw new RecordNotFoundException("no car found with license-plate: " + licenseplate);
        } else {
            return transferCarToDto(car);
        }
    }

    public Iterable<CarOutputDto> getAllCarsfromUser() {
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            if (currentuser.isPresent()) {
                User user = currentuser.get();
                ArrayList<CarOutputDto> carOutputDtos = new ArrayList<>();
                Iterable<Car> allcars = carRepository.findByUser(user);
                for (Car a : allcars) {
                    CarOutputDto carDto = transferCarToDto(a);
                    carOutputDtos.add(carDto);
                }
                return carOutputDtos;
            } else {
                throw new RecordNotFoundException("this users seems to have no values");
            }
        }
        throw new RecordNotFoundException("no user is logged in at the moment");
    }

    public String getAllCarsStatusfromUser() {
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            if (currentuser.isPresent()) {
                StringBuilder status = new StringBuilder();
                User user = currentuser.get();
                Iterable<Car> allcars = carRepository.findByUser(user);
                for (Car a : allcars) {
                    status.append(a.getLicenseplate());
                    status.append(" STATUS : ");
                    status.append(a.getCarstatus());
                    status.append("\n");
                }
                return status.toString();
            } else {
                throw new RecordNotFoundException("this users seems to have no values");
            }
        }
        throw new RecordNotFoundException("no User is logged in at the moment");
    }


    public long createCar(CarInputDto carInputDto) {

        String licenseplate = carInputDto.getLicenseplate();
        // next 2 lines check if it is a valid Dutch license plate
        if (!validateLicensePlate(licenseplate))
            throw new RecordNotFoundException("This is not a valid Dutch license plate");
        // next if statement checks if there is already a car with that license-plate
        Car car = carRepository.findBylicenseplate(licenseplate);
        if (car == null) {
            Car newcar = transferDtotoCar(carInputDto);
            Car savedcar = carRepository.save(newcar);
            // voeg onderdelen toe met lege waarden
            CarPart Tires = new CarPart(CarpartName.TIRES, "", false);
            CarPart newTires = carpartRepository.save(Tires);
            newTires.setCar(savedcar);

            CarPart Brakes = new CarPart(CarpartName.BRAKES, "", false);
            CarPart newBrakes = carpartRepository.save(Brakes);
            newBrakes.setCar(savedcar);

            CarPart steering = new CarPart(CarpartName.STEERING_LINING, "", false);
            CarPart newsteering = carpartRepository.save(steering);
            newsteering.setCar(savedcar);

            CarPart Lights = new CarPart(CarpartName.LIGHTS, "", false);
            CarPart newLights = carpartRepository.save(Lights);
            newLights.setCar(savedcar);

            CarPart Suspension = new CarPart(CarpartName.SUSPENSION, "", false);
            CarPart newSuspension = carpartRepository.save(Suspension);
            newSuspension.setCar(savedcar);

            CarPart schock_absorption = new CarPart(CarpartName.SCHOCK_ABSORPTION, "", false);
            CarPart newschock_absorption = carpartRepository.save(schock_absorption);
            newschock_absorption.setCar(savedcar);

            Optional<CarPaper> optionalcarpaper = carPaperRepository.findById(licenseplate);
            if (optionalcarpaper.isPresent()){
                CarPaper carpaper = optionalcarpaper.get();
                savedcar.setCarpaper(carpaper);
            }
            savedcar = carRepository.save(savedcar);
            return savedcar.getId();
        } else if (car.getLicenseplate().equals(licenseplate)) {
            throw new RecordNotFoundException("This: " + licenseplate + " license-plate is already registered. ");
        } else {
            throw new RecordNotFoundException("Something went wrong");
        }
    }

    public CarOutputDto updateCar(long id, CarOutputDto carOutputDto) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()) {
            throw new RecordNotFoundException("no car with id: " + id);
        } else {
            Car updatedcar = car.get();
            updatedcar.setBrand(carOutputDto.getBrand());
            updatedcar.setLicenseplate(carOutputDto.getLicenseplate());
            updatedcar.setCarstatus(carOutputDto.getCarstatus());
            carRepository.save(updatedcar);
            return transferCarToDto(updatedcar);
        }

    }


    public CarOutputDto updateCarStatusDesk(String licenseplate, String carstatus) {
        Car car = carRepository.findBylicenseplate(licenseplate);
        carStatusValidation(carstatus);
        Carstatus status = Carstatus.valueOf(carstatus);
        if (car == null) {
            throw new RecordNotFoundException("no car found with license-plate: " + licenseplate);
        } else if (status != Carstatus.CHECKED_IN && status != Carstatus.PICKED_UP) {
            throw new RecordNotFoundException("You'r cuurent role doesn't allowed you to do this");
        } else {
            car.setCarstatus(status);
            carRepository.save(car);
            return transferCarToDto(car);
        }
    }

    public CarOutputDto updateCarStatusMechanic(String licenseplate, String carstatus) {
        Car car = carRepository.findBylicenseplate(licenseplate);
        carStatusValidation(carstatus);
        Carstatus status = Carstatus.valueOf(carstatus);
        if (car == null) {
            throw new RecordNotFoundException("no car found with license-plate: " + licenseplate);
        } else if (status != Carstatus.INSPECTING && status != Carstatus.AWAITING_APPROVAL && status != Carstatus.REPAIR && status != Carstatus.WASHING && status != Carstatus.READY) {
            throw new BadRequestException("You are not allowed to do this");
        } else {
            List<CarPart> carpartsinspected = car.getCarparts();
            for (CarPart carpart : carpartsinspected) {
                if (!carpart.isChecked()) {
                    throw new BadRequestException("You can not proceed without Inspecting all the carparts!");
                }
            }
            car.setCarstatus(status);
            carRepository.save(car);
            String email = car.getUser().getEmail();
            if (car.getCarstatus() == Carstatus.AWAITING_APPROVAL) {
                Email approvalmail = new Email(//
                        email,//
                        "repair approval",//
                        "Your car is awaiting approval for the repairs. " +
                                "If you dont want all or none of the repairs please give us a call!");
                this.emailService.sendSimpleMail(approvalmail);
            }
            if (car.getCarstatus() == Carstatus.READY) {
                Email readymail = new Email(//
                        email,//
                        "Your car is ready",//
                        "Your car is awaiting pickup thank you for choosing transparant garage.");
                this.emailService.sendSimpleMail(readymail);
            }
            return transferCarToDto(car);
        }
    }

    public String deleteCar(long id) {
        Optional<Car> optionalcar = carRepository.findById(id);
        if (optionalcar.isEmpty()) {
            throw new RecordNotFoundException("No car found with the id of : " + id);
        } else {
            try {
            Car car = optionalcar.get();
            carRepository.delete(car);
            return "Car Removed successfully";
            } catch (Exception e) {
                throw new BadRequestException("it seems that this is still connected to the car, a carpart, a maintenance or a invoice. First delete this/these for deleting this.");
            }
        }
    }

    private CarOutputDto transferCarToDto(Car car) {
        CarOutputDto autoDto = new CarOutputDto();

        if (car.getLicenseplate() != null) {
            autoDto.setLicenseplate(car.getLicenseplate());
        }
        if (car.getBrand() != null) {
            autoDto.setBrand(car.getBrand());
        }
        if (car.getCarstatus() != null) {
            autoDto.setCarstatus(car.getCarstatus());
        }
        if (car.getCarparts() != null) {
            autoDto.setCarparts(car.getCarparts());
        }
        if (car.getUser() != null) {
            autoDto.setUser(car.getUser());
        }
        return autoDto;
    }

    private Car transferDtotoCar(CarInputDto carInputDto) {
        Car car = new Car();

        car.setLicenseplate(carInputDto.getLicenseplate());
        car.setBrand(carInputDto.getBrand());
        car.setCarstatus(carInputDto.getCarstatus());
        car.setCarparts(carInputDto.getCarparts());
        car.setUser(carInputDto.getUser());

        return car;

    }

    public void carStatusValidation(String carstatus){
        String errormessage = "Not the correct input. Input for status needs to be one of these:  CHECKED_IN, INSPECTING, AWAITING_APPROVAL, REPAIR, WASHING, READY, PICKED_UP";
        if (
                !Objects.equals(carstatus, "CHECKED_IN")&
                        !Objects.equals(carstatus, "INSPECTING" )&
                        !Objects.equals(carstatus, "AWAITING_APPROVAL" )&
                        !Objects.equals(carstatus, "REPAIR" )&
                        !Objects.equals(carstatus, "WASHING" )&
                        !Objects.equals(carstatus, "READY" )&
                        !Objects.equals(carstatus, "PICKED_UP" )){
            throw new BadRequestException(errormessage);
        }
    }



}
