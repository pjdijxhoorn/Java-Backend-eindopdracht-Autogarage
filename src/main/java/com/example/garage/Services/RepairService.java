package com.example.garage.Services;

import com.example.garage.Dtos.Input.RepairInputDto;
import com.example.garage.Dtos.Output.RepairOutputDto;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.CarPart;
import com.example.garage.Models.CarService;
import com.example.garage.Models.Repair;
import com.example.garage.Repositories.CarServiceRepository;
import com.example.garage.Repositories.CarpartRepository;
import com.example.garage.Repositories.RepairRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class RepairService {

    private final RepairRepository repairRepository;
    private final CarServiceRepository carServiceRepository;
    private final CarpartRepository carpartRepository;

    public RepairService(RepairRepository repairRepository, CarServiceRepository carServiceRepository, CarpartRepository carpartRepository) {
        this.repairRepository = repairRepository;
        this.carServiceRepository = carServiceRepository;
        this.carpartRepository = carpartRepository;
    }

    public RepairOutputDto getOneRepairByID(long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        if (repair.isEmpty()) {
            throw new RecordNotFoundException("no repair found with id: " + id);
        } else {
            Repair repair1 = repair.get();
            return transferRepairtoDto(repair1);
        }
    }

    public long createRepair(RepairInputDto repairInputDto, String carpart, long carservice_id) {
        Optional<CarService> carservice = carServiceRepository.findById(carservice_id);
        if (carservice.isEmpty()) {
            throw new RecordNotFoundException("this carservice seems to be non existing : " + carservice_id);
        } else {
            CarService carservice1 = carservice.get();
            CarPart carpart1 = new CarPart();
            //next lines are to get the right carpart by name easier for the mechanic then id for every car has the same basic components
            for (CarPart carpartx : carservice1.getCar().getCarparts()) {
                String carpartname = String.valueOf(carpartx.getCarpartname());
                if (Objects.equals(carpartname, carpart)) {
                    carpart1 = carpartRepository.getById(carpartx.getId());
                }
            }
            Repair newrepair = transferDtotoRepair(repairInputDto);
            newrepair.setCarpart(carpart1);
            newrepair.setCarservice(carservice1);
            Repair savedrepair = repairRepository.save(newrepair);
            return savedrepair.getId();
        }
    }

    public RepairOutputDto SetRepaired(long id, RepairInputDto repairInputDto) {
        Optional<Repair> repair = repairRepository.findById(id);
        if (repair.isEmpty()) {
            throw new RecordNotFoundException("no repair with id: " + id);
        } else {
            Repair updatedrepair = repair.get();
            if (!updatedrepair.getCarservice().isRepair_approved()) {
                throw new BadRequestException("The customer hasn't approved of the repairs yet");
            } else {
                updatedrepair.setRepair_done(repairInputDto.isRepair_done());
                repairRepository.save(updatedrepair);
                return transferRepairtoDto(updatedrepair);
            }
        }
    }

    public String deleteRepair(long id) {
        Optional<Repair> optionalrepair = repairRepository.findById(id);
        if (optionalrepair.isEmpty()) {
            throw new RecordNotFoundException("No repair found with the id of : " + id);
        } else {
            Repair repair = optionalrepair.get();
            repairRepository.delete(repair);
            return "Repair Removed successfully";
        }
    }


    private RepairOutputDto transferRepairtoDto(Repair repair) {
        RepairOutputDto repairDto = new RepairOutputDto();

        repairDto.setRepairCost(repair.getRepairCost());
        repairDto.setNotes(repair.getNotes());
        repairDto.setCarService(repair.getCarservice());
        repairDto.setCarpart(repair.getCarpart());

        return repairDto;
    }

    private Repair transferDtotoRepair(RepairInputDto repairInputDto) {
        Repair repair = new Repair();

        repair.setRepairCost(repairInputDto.getRepairCost());
        repair.setNotes(repairInputDto.getNotes());
        repair.setCarservice(repairInputDto.getCarService());
        repair.setCarpart(repairInputDto.getCarpart());
        return repair;
    }


}
