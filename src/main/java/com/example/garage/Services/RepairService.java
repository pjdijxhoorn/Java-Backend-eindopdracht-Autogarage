package com.example.garage.Services;

import com.example.garage.Dtos.Input.RepairInputDto;
import com.example.garage.Dtos.Output.RepairOutputDto;
import com.example.garage.Models.Repair;
import com.example.garage.Repositories.RepairRepository;
import org.springframework.stereotype.Service;

@Service
public class RepairService {

   private final RepairRepository repairRepository;

   public RepairService(RepairRepository repairRepository) {
      this.repairRepository = repairRepository;
   }

   public long createRepair(RepairInputDto repairInputDto){
      // if approval.service boalean == true
      Repair newrepair = transferDtotoRepair(repairInputDto);
      Repair savedrepair = repairRepository.save(newrepair);
      return savedrepair.getId();

   }

   private RepairOutputDto transferRepairtoDto(Repair repair){
      RepairOutputDto repairDto = new RepairOutputDto();

      repairDto.setRepairCost(repair.getRepairCost());
      repairDto.setNotes(repair.getNotes());
      repairDto.setService(repair.getService());
      repairDto.setCarpart(repair.getCarpart());

      return repairDto;
   }

   private Repair transferDtotoRepair(RepairInputDto repairInputDto){
      Repair repair = new Repair();

      repair.setRepairCost(repairInputDto.getRepairCost());
      repair.setNotes(repairInputDto.getNotes());
      repair.setService(repairInputDto.getService());
      repair.setCarpart(repairInputDto.getCarpart());
      return repair;
   }




}
