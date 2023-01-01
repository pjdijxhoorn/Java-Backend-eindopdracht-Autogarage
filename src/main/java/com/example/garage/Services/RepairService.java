package com.example.garage.Services;

import com.example.garage.Dtos.Input.RepairInputDto;
import com.example.garage.Dtos.Output.RepairOutputDto;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.Car;
import com.example.garage.Models.CarPart;
import com.example.garage.Models.CarService;
import com.example.garage.Models.Repair;
import com.example.garage.Repositories.CarpartRepository;
import com.example.garage.Repositories.RepairRepository;
import com.example.garage.Repositories.CarServiceRepository;
import org.springframework.stereotype.Service;

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
      if (repair.isEmpty()){
         throw new RecordNotFoundException("no repair found with id: "+ id);
      }else {
         Repair repair1 = repair.get();
         return transferRepairtoDto(repair1);
      }
   }

   public long createRepair(RepairInputDto repairInputDto, long carpart_id, long carservice_id){
      Optional<CarService> carservice = carServiceRepository.findById(carservice_id);
      Optional<CarPart> carpart = carpartRepository.findById(carpart_id);
      if (carservice.isEmpty()){
         throw new RecordNotFoundException("this service seems to be non existing : " + carservice_id);
      } else if (carpart.isEmpty()) {
         throw new RecordNotFoundException("this carpart seems to be non existing : " + carpart_id);
      }else{
         CarService carservice1 = carservice.get();
         CarPart carpart1 = carpart.get();
         Repair newrepair = transferDtotoRepair(repairInputDto);
         newrepair.setCarpart(carpart1);
         newrepair.setCarservice(carservice1);
         Repair savedrepair = repairRepository.save(newrepair);
         return savedrepair.getId();
      }
   }

   public RepairOutputDto SetRepaired(long id,RepairInputDto repairInputDto){
      Optional<Repair> repair = repairRepository.findById(id);
      if (repair.isEmpty()){
         throw new RecordNotFoundException("no repair with id: " + id );
      }else {
         Repair updatedrepair = repair.get();
         if (!updatedrepair.getCarservice().isRepair_approved()) {
            throw new BadRequestException("The customer hasn't approved of the repairs yet");
         }else{
            updatedrepair.setRepair_done(repairInputDto.isRepair_done());
            repairRepository.save(updatedrepair);
            return transferRepairtoDto(updatedrepair);
         }
      }
   }

   private RepairOutputDto transferRepairtoDto(Repair repair){
      RepairOutputDto repairDto = new RepairOutputDto();

      repairDto.setRepairCost(repair.getRepairCost());
      repairDto.setNotes(repair.getNotes());
      repairDto.setCarService(repair.getCarservice());
      repairDto.setCarpart(repair.getCarpart());

      return repairDto;
   }

   private Repair transferDtotoRepair(RepairInputDto repairInputDto){
      Repair repair = new Repair();

      repair.setRepairCost(repairInputDto.getRepairCost());
      repair.setNotes(repairInputDto.getNotes());
      repair.setCarservice(repairInputDto.getCarService());
      repair.setCarpart(repairInputDto.getCarpart());
      return repair;
   }




}
