package com.example.garage.Services;

import com.example.garage.Dtos.Output.MaintenanceOutputDto;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.Car;
import com.example.garage.Models.Maintenance;
import com.example.garage.Models.User;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.MaintenanceRepository;
import com.example.garage.Repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MaintenanceService {

    private final CarRepository carRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final UserRepository userRepository;


    public MaintenanceService(CarRepository carRepository, MaintenanceRepository maintenanceRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.userRepository = userRepository;
    }

    public Iterable<MaintenanceOutputDto> getAllCarServices() {
        ArrayList<MaintenanceOutputDto> maintenanceOutputDtos = new ArrayList<>();
        Iterable<Maintenance> allcarmaintenance = maintenanceRepository.findAll();
        for (Maintenance a : allcarmaintenance) {
            MaintenanceOutputDto carserviceDto = transferMaintenancetoOuputDto(a);
            maintenanceOutputDtos.add(carserviceDto);
        }
        return maintenanceOutputDtos;
    }

    public Iterable<MaintenanceOutputDto> getAllCarServiceFromUser() {
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            if (currentuser.isPresent()) {
                User user = currentuser.get();
                ArrayList<MaintenanceOutputDto> carserviceOutputDtos = new ArrayList<>();
                Iterable<Car> allcars = carRepository.findByUser(user);
                for (Car a : allcars) {
                    Iterable<Maintenance> carservices = a.getMaintenances();
                    for (Maintenance b : carservices) {
                        MaintenanceOutputDto carserviceDto = transferMaintenancetoOuputDto(b);
                        carserviceOutputDtos.add(carserviceDto);
                    }
                }
                return carserviceOutputDtos;
            } else {
                throw new RecordNotFoundException("this users seems to have no values");
            }
        }
        throw new RecordNotFoundException("no User is logged in at the moment");
    }

    public MaintenanceOutputDto getOneCarServiceByID(long id) {
        Optional<Maintenance> optionalmaintenance = maintenanceRepository.findById(id);
        if (optionalmaintenance.isEmpty()) {
            throw new RecordNotFoundException("no carservice found with id: " + id);
        } else {
            Maintenance carservice = optionalmaintenance.get();
            return transferMaintenancetoOuputDto(carservice);
        }
    }


    public MaintenanceOutputDto createCarService(long car_id) {
        Optional<Car> optionalcar = carRepository.findById(car_id);
        if (optionalcar.isEmpty()) {
            throw new RecordNotFoundException("No car found with the id of : " + car_id);
        } else {
            Car car = optionalcar.get();
            Maintenance newmaintenance = new Maintenance();
            newmaintenance.setCar(car);
            newmaintenance.setRepair_approved(false);
            newmaintenance.setCustumor_response(false);
            newmaintenance.setMechanic_done(false);
            newmaintenance.setTotalrepaircost(newmaintenance.calculateRepairCost());

            Maintenance savedmaintenance = maintenanceRepository.save(newmaintenance);

            return transferMaintenancetoOuputDto(savedmaintenance);
        }
    }

    public MaintenanceOutputDto mechanicIsDone(long id, MaintenanceOutputDto maintenanceOutputDto) {
        Optional<Maintenance> optionalmaintenance = maintenanceRepository.findById(id);
        if (optionalmaintenance.isEmpty()) {
            throw new RecordNotFoundException("couldnt find the carservice with this id: " + id);
        } else {
            Maintenance maintenance = optionalmaintenance.get();
            maintenance.setMechanic_done(maintenanceOutputDto.isMechanic_done());
            maintenanceRepository.save(maintenance);
            return transferMaintenancetoOuputDto(maintenance);
        }


    }

    public String approvalUser(long id, MaintenanceOutputDto maintenanceOutputDto) {
        // get the current user
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findById(id);
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            // check if current user is a logged in user and if the carservice exists
            if (currentuser.isEmpty()) {
                throw new RecordNotFoundException("this users seems to have no values");
            } else if (optionalMaintenance.isEmpty()) {
                throw new RecordNotFoundException("couldnt find the carservice with this id: " + id);
            } else {
                System.out.println("1");
                User user = currentuser.get();
                Maintenance maintenance = optionalMaintenance.get();
                // check if the user owns the car from the carservice he is about to approve
                boolean doescarbelong = false;
                for (Car a : user.getCars()) {
                    if (a == maintenance.getCar()) {
                        doescarbelong = true;
                        System.out.println("2");
                        // if car is found break
                        break;
                    }
                }
                // if car is of the user set approve
                if (doescarbelong) {
                    System.out.println("3");
                    maintenance.setRepair_approved(maintenanceOutputDto.isRepair_approved());
                    maintenance.setCustumor_response(true);
                    maintenanceRepository.save(maintenance);
                    if (maintenance.isRepair_approved()) {
                        return "Repair approved";
                    } else {
                        return "Repair is not approved. Service will continu without repairs";
                    }
                } else {
                    throw new RecordNotFoundException("you don't own this car so you can not approve the repairs.");
                }
            }
        }
        throw new RecordNotFoundException("no User is logged in at the moment");
    }

    public String deletecarservice(long id) {
        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findById(id);
        if (optionalMaintenance.isEmpty()) {
            throw new RecordNotFoundException("No service found with the id of : " + id);
        } else {
            try {
                Maintenance maintenance = optionalMaintenance.get();
                maintenanceRepository.delete(maintenance);
                return "Repair Removed successfully";
            } catch (Exception e) {
                throw new BadRequestException("it seems that this is still connected to a repair, a car or a invoice. First delete this/these for deleting this.");
            }
        }

    }


    private MaintenanceOutputDto transferMaintenancetoOuputDto(Maintenance maintenance) {
        MaintenanceOutputDto maintenanceOutputDto = new MaintenanceOutputDto();
        maintenanceOutputDto.setCar(maintenance.getCar());
        maintenanceOutputDto.setRepairs(maintenance.getRepairs());
        maintenanceOutputDto.setInvoice(maintenance.getInvoice());

        maintenanceOutputDto.setRepair_approved(maintenance.isRepair_approved());
        maintenanceOutputDto.setCustumor_response(maintenance.isCustumor_response());
        maintenanceOutputDto.setMechanic_done(maintenance.isMechanic_done());
        maintenanceOutputDto.setTotalrepaircost(maintenanceOutputDto.calculateRepairCost());
        return maintenanceOutputDto;
    }
}


