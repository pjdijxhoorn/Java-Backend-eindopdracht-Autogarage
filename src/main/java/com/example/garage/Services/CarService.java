package com.example.garage.Services;

import com.example.garage.Dtos.Input.CarInputDto;
import com.example.garage.Dtos.Output.CarOutputDto;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.*;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.CarpartRepository;
import com.example.garage.Repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;



@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarpartRepository carpartRepository;
    private final UserRepository userRepository;
    public CarService(CarRepository carRepository, CarpartRepository carpartRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.carpartRepository = carpartRepository;
        this.userRepository = userRepository;
    }


    public Iterable<CarOutputDto> getAllCars() {
        ArrayList<CarOutputDto> carOutputDtos = new ArrayList<>();
        Iterable<Car> allcars = carRepository.findAll();
        for (Car a: allcars){
            CarOutputDto AutoDto = transferCarToDto(a);
            carOutputDtos.add(AutoDto);
        }
        return carOutputDtos;
    }
    public CarOutputDto getOneCarByID(long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()){
            throw new RecordNotFoundException("no car found with id: "+ id);
        }else {
            Car car1 = car.get();
            return transferCarToDto(car1);
        }
    }
    public CarOutputDto getOneCarByLincensePlate(String licenseplate) {
        Car car = carRepository.findBylicenseplate(licenseplate);
        if (car == null){
            throw new RecordNotFoundException("no car found with license-plate: "+ licenseplate);
        }
        else {
            return transferCarToDto(car);}
    }
    public Iterable<CarOutputDto> getAllCarsfromUser() {
        String currentUserName = new String();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            if (currentuser.isPresent()){
                User user = currentuser.get();
                ArrayList<CarOutputDto> carOutputDtos = new ArrayList<>();
                Iterable<Car> allcars = carRepository.findByUser(user);
                for (Car a: allcars){
                    CarOutputDto carDto = transferCarToDto(a);
                    carOutputDtos.add(carDto);
                }
                return carOutputDtos;
            }else {
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
        if (car == null){
            Car newcar = transferDtotoCar(carInputDto);
            Car savedcar = carRepository.save(newcar);
            // voeg onderdelen toe met lege waarden
            CarPart Tires = new CarPart(CarpartName.TIRES,"",false);
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

            savedcar = carRepository.save(savedcar);
            return savedcar.getId();
        } else if (car.getLicenseplate().equals( licenseplate)) {
            throw new RecordNotFoundException("This: " + licenseplate + " license-plate is already registered. ");
        } else {
            throw new RecordNotFoundException("Something went wrong");
        }
    }
    public CarOutputDto updateCar (long id, CarOutputDto carOutputDto){
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()){
            throw new RecordNotFoundException("no car with id: " + id );
        }else {
            Car updatedcar = car.get();
            updatedcar.setBrand(carOutputDto.getBrand());
            updatedcar.setLicenseplate(carOutputDto.getLicenseplate());
            updatedcar.setCarstatus(carOutputDto.getCarstatus());
            carRepository.save(updatedcar);
            return transferCarToDto(updatedcar);
        }

    }

    public CarOutputDto updateCarStatusDesk(long id, CarOutputDto carOutputDto){
        Optional<Car> car = carRepository.findById(id);
        Carstatus status = carOutputDto.getCarstatus();
        if (car.isEmpty()) {
            throw new RecordNotFoundException("no car with id: " + id);
        }else if (status != Carstatus.CHECKED_IN && status != Carstatus.PICKED_UP){
            throw new RecordNotFoundException("You are not allowed to do this");
        }else {
            Car updatedcar = car.get();
            updatedcar.setCarstatus(carOutputDto.getCarstatus());
            return transferCarToDto(updatedcar);
    }
    }

    public CarOutputDto updateCarStatusMechanic(long id, CarOutputDto carOutputDto){
        Optional<Car> car = carRepository.findById(id);
        Carstatus status = carOutputDto.getCarstatus();
        if (car.isEmpty()) {
            throw new RecordNotFoundException("no car with id: " + id);
        }else if (status != Carstatus.INSPECTING && status != Carstatus.AWAITING_APPROVAL && status != Carstatus.REPAIR && status != Carstatus.WASHING && status != Carstatus.READY){
            throw new RecordNotFoundException("You are not allowed to do this");
        }else {
            Car updatedcar = car.get();
            updatedcar.setCarstatus(carOutputDto.getCarstatus());
            return transferCarToDto(updatedcar);
        }
    }

    public String deleteCar(long id) {
        Optional<Car> auto = carRepository.findById(id);
        if (auto.isEmpty()){
            throw new RecordNotFoundException("No car found with the id of : "+ id);
        }
        else {
            Car car1 = auto.get();
            carRepository.delete(car1);
            return "Car Removed successfully";}
    }
    private CarOutputDto transferCarToDto(Car car) {
        CarOutputDto autoDto = new CarOutputDto();

        if (car.getLicenseplate() !=null){
            autoDto.setLicenseplate(car.getLicenseplate());
        }
        if (car.getBrand() !=null){
            autoDto.setBrand(car.getBrand());
        }
        if (car.getCarstatus() !=null){
            autoDto.setCarstatus(car.getCarstatus());
        }
        if (car.getCarparts() != null) {
            autoDto.setCarparts(car.getCarparts());
        }
        if(car.getUser() != null) {
            autoDto.setUser(car.getUser());
        }

        return autoDto;
    }

    private Car transferDtotoCar(CarInputDto carInputDto){
        Car car = new Car();

        car.setLicenseplate(carInputDto.getLicenseplate());
        car.setBrand(carInputDto.getBrand());
        car.setCarstatus(carInputDto.getCarstatus());
        car.setCarparts(carInputDto.getCarparts());
        car.setUser(carInputDto.getUser());

        return car;

    }


    private Boolean validateLicensePlate(String licenseplate){
        if (licenseplate.matches("..-..-..")){
            // Dutch cars from 1951 to 2005
            return true;
        } else if (licenseplate.matches("^[0-9][0-9]-[A-Z][A-Z][A-Z]-[0-9]$")){
            // Dutch cars from 2005
            return true;}
        else if (licenseplate.matches("^[0-9]-[A-Z][A-Z][A-Z]-[0-9][0-9]$")){
            // Dutch cars from 2009
            return true;
        } else if (licenseplate.matches("^[A-Z][A-Z]-[0-9][0-9][0-9]-[A-Z]$")){
            // Dutch cars from 2006
            return true;
        }
        else if (licenseplate.matches("^[A-Z]-[0-9][0-9][0-9]-[A-Z][A-Z]$")){
            // Dutch cars from 2008
            return true;
        }
        else if (licenseplate.matches("^[A-Z][A-Z][A-Z]-[0-9][0-9]-[A-Z]$")){
            // Dutch cars from 2015
            return true;
        }
        else if (licenseplate.matches("^[0-9]-[A-Z][A-Z]-[0-9][0-9][0-9]$")){
            // Dutch cars from 2016
            return true;
        }
        else if (licenseplate.matches("^[0-9][0-9][0-9]-[A-Z][A-Z]-[0-9]$")){
            // Dutch cars from 2019
            return true;
        }else {
            return false;
    }
    }
}
