package com.example.garage.Services;

import com.example.garage.Dtos.Input.RepairInputDto;
import com.example.garage.Dtos.Output.RepairOutputDto;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.*;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.CarpartRepository;
import com.example.garage.Repositories.MaintenanceRepository;
import com.example.garage.Repositories.RepairRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static com.example.garage.Models.Carstatus.CHECKED_IN;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RepairServiceTest {

    @Mock
    RepairRepository repairRepository;
    @Mock
    MaintenanceRepository maintenanceRepository;
    @Mock
    CarpartRepository carpartRepository;
    @Mock
    CarRepository carRepository;

    @InjectMocks
    RepairService repairService;

    @Captor
    ArgumentCaptor<Repair> captor;
    Car car1;
    Car car2;
    Repair repair1;
    Repair repair2;
    Repair repair3;
    Repair repair4;
    RepairInputDto repairDto1;
    RepairInputDto repairDto2;
    Maintenance maintenance1;
    Maintenance maintenance2;
    Maintenance maintenance3;
    CarPart tires;
    CarPart brakes;

    @BeforeEach
    void setUp() {

        car1 = new Car(1L,"33-TTB-3","TOYOTA",CHECKED_IN,null,null,null,null,null);
        car2 = new Car(2L,"33-TTB-4","TOYOTA",CHECKED_IN,null,null,null,null,null);

        maintenance1 = new Maintenance(1L, false, true,false,0,null,car1,null);
        maintenance2 = new Maintenance(2L, true, true,false,0,null, car1,null);
        maintenance3 = new Maintenance(3L, true, true,false,0,null, car1,null);
        tires = new CarPart(2L,CarpartName.TIRES,"3mm profile",true,car1,null);
        tires.setCar(car1);
        repair1 = new Repair(1L, 100.00, "worn out brakes",false,tires, maintenance2);
        brakes = new CarPart(1L, CarpartName.BRAKES,"3mm profile",true,car1,null);
        brakes.setCar(car1);
        repair2 = new Repair(2L, 100.00, "worn out brakes",false,brakes, maintenance2);
        repair3 = new Repair(3L, 100.00, "worn out brakes",false,brakes, maintenance1);
        repair4 = new Repair(4L, 100.00, "worn out brakes",false,null, null);
        repairDto1 = new RepairInputDto(100.00,"worn out brakes",tires,maintenance1,true);
        repairDto2 = new RepairInputDto(100.00,"worn out brakes",brakes, maintenance2,true);

        ArrayList<CarPart> carparts = new ArrayList<>();
        carparts.add(tires);
        carparts.add(brakes);
        car1.setCarparts(carparts);

        ArrayList<Maintenance> maintenances = new ArrayList<>();
        maintenances.add(maintenance1);
        maintenances.add(maintenance2);
        car1.setMaintenances(maintenances);

        ArrayList<Maintenance> maintenances1 = new ArrayList<>();
        car2.setMaintenances(maintenances1);

        ArrayList<Repair> repairs = new ArrayList<>();
        repairs.add(repair1);
        repairs.add(repair2);
        repairs.add(repair3);
        maintenance2.setRepairs(repairs);

    }


    // testen..........................................................
    @Test
    void getAllRepairsFromOneCar() {
        when(carRepository.findBylicenseplate(any())).thenReturn(car1);

        List<RepairOutputDto> repairsfound = (List<RepairOutputDto>) repairService.getAllRepairsFromOneCar("33-TTB-3");
        assertEquals(repair1.getRepairCost(), repairsfound.get(0).getRepairCost());
        assertEquals(repair2.getRepairCost(), repairsfound.get(1).getRepairCost());
        assertEquals(repair2.getCarpart(), repairsfound.get(1).getCarpart());
        assertEquals(repair2.getMaintenance(), repairsfound.get(1).getMaintenance());
    }
    @Test
    void getAllRepairsFromOneCarThrowsExceptionForRepairTest() {
            assertThrows(RecordNotFoundException.class, () -> repairService.getAllRepairsFromOneCar("33-TTB-3"));

            when(carRepository.findBylicenseplate(any())).thenReturn(car2);
        assertThrows(RecordNotFoundException.class, () -> repairService.getAllRepairsFromOneCar("33-TTB-4"));
    }


    @Test
    void createRepair() {
        when(maintenanceRepository.findById(2L)).thenReturn(Optional.of(maintenance2));
        when(carpartRepository.findById(2L)).thenReturn(Optional.of(tires));
        when(repairRepository.save(any())).thenReturn(repair1);

        repairService.createRepair(repairDto1,"TIRES",2L);
        verify(repairRepository, times(1)).save(captor.capture());
        Repair captured = captor.getValue();

        assertEquals(repair1.getRepairCost(), captured.getRepairCost());
        assertEquals(repair1.getNotes(), captured.getNotes());
        assertEquals(repair1.getMaintenance(), captured.getMaintenance());
        assertEquals(repair1.getCarpart(), captured.getCarpart());


    }

    @Test
    void setRepaired() {
        when(repairRepository.findById(1L)).thenReturn(Optional.of(repair1));
        when(repairRepository.existsById(1L)).thenReturn(true);
        when(repairRepository.save(any())).thenReturn(repair1);

        repairService.SetRepaired(1L, repairDto1);

        verify(repairRepository, times(1)).save(captor.capture());
        Repair captured = captor.getValue();

        assertEquals(repair1.getId(), captured.getId());
        assertEquals(repair1.isRepair_done(), captured.isRepair_done());

    }
    @Test
    void setRepairedThrowsExceptionForRepairTest() {
        assertThrows(RecordNotFoundException.class, () -> repairService.SetRepaired(3L,repairDto1));

    }

    @Test
    void setRepairedThrowsBadRequestExceptionForRepairTest() {
        when(repairRepository.findById(3L)).thenReturn(Optional.of(repair3));
        assertThrows(BadRequestException.class, () -> repairService.SetRepaired(3L,repairDto2));
    }

    @Test
    void deleteRepair() {
        when(repairRepository.existsById(1L)).thenReturn(true);
        when(repairRepository.findById(1L)).thenReturn(Optional.of(repair1));
        repairService.deleteRepair(1L);

        verify(repairRepository).delete(repair1);

    }

    @Test
    void transferDtoToRepair(){
        Repair repairreturn = repairService.transferDtoToRepair(repairDto1);
        assertEquals(repairDto1.getRepairCost(), repairreturn.getRepairCost());
        assertEquals(repairDto1.getNotes(), repairreturn.getNotes());
        assertEquals(repairDto1.getMaintenance(), repairreturn.getMaintenance());
        assertEquals(repairDto1.getCarpart(), repairreturn.getCarpart());
    }
}

