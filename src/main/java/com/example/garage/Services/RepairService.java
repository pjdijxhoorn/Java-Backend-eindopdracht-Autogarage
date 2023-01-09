package com.example.garage.Services;

import com.example.garage.Dtos.Input.RepairInputDto;
import com.example.garage.Dtos.Output.RepairOutputDto;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.Car;
import com.example.garage.Models.CarPart;
import com.example.garage.Models.Maintenance;
import com.example.garage.Models.Repair;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.MaintenanceRepository;
import com.example.garage.Repositories.CarpartRepository;
import com.example.garage.Repositories.RepairRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RepairService {

    private final RepairRepository repairRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final CarpartRepository carpartRepository;
    private final CarRepository carRepository;

    public RepairService(RepairRepository repairRepository, MaintenanceRepository maintenanceRepository, CarpartRepository carpartRepository, CarRepository carRepository) {
        this.repairRepository = repairRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.carpartRepository = carpartRepository;
        this.carRepository = carRepository;
    }

    public Iterable<RepairOutputDto> getAllRepairsFromOneCar(String licenseplate) {
        Car car = carRepository.findBylicenseplate(licenseplate);
        if (car == null) {
            throw new RecordNotFoundException("no car found with license-plate: " + licenseplate);
        } else {
            ArrayList<RepairOutputDto> repairOutputDtos = new ArrayList<>();
            List<Maintenance> maintenances;
            maintenances = car.getMaintenances();
            if (maintenances.size() > 0) {
                // This code gets the last car service to get all the repairs from that service.
                // Getting the last service is ok because 1 car can only have one service at the same time.
                Maintenance lastmaintenance = maintenances.get(maintenances.size() - 1);
                for (Repair repair : lastmaintenance.getRepairs()) {
                    RepairOutputDto repairOutputDto = transferRepairToDto(repair);
                    repairOutputDtos.add(repairOutputDto);
                }
            } else {
                throw new RecordNotFoundException("no repairs found for this car");
            }
            return repairOutputDtos;
        }
    }

    public long createRepair(RepairInputDto repairInputDto, String carpart, long carservice_id) {
        Maintenance maintenance = maintenanceRepository.findById(carservice_id)
                .orElseThrow(() -> new RecordNotFoundException("No carcarservice found with id: " + carservice_id));
        CarPart carpart1 = new CarPart();
        // Next lines are to get the right carpart by name.
        // This is easier for the mechanic then id for every car has the same basic components.
        for (CarPart carpartx : maintenance.getCar().getCarparts()) {
            String carpartname = String.valueOf(carpartx.getCarpartname());
            if (Objects.equals(carpartname, carpart)) {
                carpart1 = carpartRepository.findById(carpartx.getId())
                        .orElseThrow(() -> new RecordNotFoundException("No carpart found to repair"));
            }
        }
        Repair newrepair = transferDtoToRepair(repairInputDto);
        newrepair.setCarpart(carpart1);
        newrepair.setMaintenance(maintenance);
        Repair savedrepair = repairRepository.save(newrepair);
        return savedrepair.getId();
    }


    public RepairOutputDto SetRepaired(long id, RepairInputDto repairInputDto) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No Repair found with id: " + id));
        if (!repair.getMaintenance().isRepair_approved()) {
            throw new BadRequestException("The customer hasn't approved of the repairs yet");
        } else {
            repair.setRepair_done(repairInputDto.isRepair_done());
            repairRepository.save(repair);
            return transferRepairToDto(repair);
        }
    }

    public String deleteRepair(long id) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No Repair found with id: " + id));
        repairRepository.delete(repair);
        return "Repair Removed successfully";
    }

    private RepairOutputDto transferRepairToDto(Repair repair) {
        RepairOutputDto repairDto = new RepairOutputDto();

        repairDto.setRepairCost(repair.getRepairCost());
        repairDto.setNotes(repair.getNotes());
        if (repairDto.getMaintenance() == null) {
            repairDto.setMaintenance(repair.getMaintenance());
        }
        if (repairDto.getCarpart() == null) {
            repairDto.setCarpart(repair.getCarpart());
        }

        return repairDto;
    }

    private Repair transferDtoToRepair(RepairInputDto repairInputDto) {
        Repair repair = new Repair();

        repair.setRepairCost(repairInputDto.getRepairCost());
        repair.setNotes(repairInputDto.getNotes());
        if (repair.getMaintenance() != null) {
            repair.setMaintenance(repairInputDto.getMaintenance());
        }
        if (repair.getCarpart() != null) {
            repair.setCarpart(repairInputDto.getCarpart());
        }

        return repair;
    }


}
