package com.example.garage.intergrationtest;

import com.example.garage.Dtos.Input.CarPartInputDto;
import com.example.garage.Models.Car;
import com.example.garage.Models.CarPart;
import com.example.garage.Models.CarpartName;
import com.example.garage.Repositories.CarRepository;
import com.example.garage.Repositories.CarpartRepository;
import com.example.garage.Services.CarpartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static com.example.garage.Models.Carstatus.CHECKED_IN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CarpartIntergrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CarRepository carRepository;

    @Autowired
    CarpartRepository carpartRepository;

    @Autowired
    CarpartService carpartService;


    Car car1;
    CarPart tires;
    CarPart brakes;
    CarPartInputDto carPartInputDto1;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
        carpartRepository.deleteAll();
        car1 = new Car(1L,"66-LOL-6","TOYOTA",CHECKED_IN,null,null,null,null,null);
        tires = new CarPart(1L, CarpartName.TIRES,"3mm profile",true,car1,null);

        ArrayList<CarPart> carparts = new ArrayList<>();
        carparts.add(tires);
        carparts.add(brakes);
        car1.setCarparts(carparts);

        carPartInputDto1 = new CarPartInputDto();
        carPartInputDto1.setChecked(false);
        carPartInputDto1.setState("no profile you fool");
        carRepository.save(car1);
    }

    @Test
    void getAllPartsFromCarid() throws Exception {
        mockMvc.perform(get("/carparts/"+ car1.getLicenseplate()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].state").value("3mm profile"))
                .andExpect(jsonPath("$[0].checked").value(true))
        ;
    }

    @Test
    void Carpartinspection() throws Exception {
        mockMvc.perform(put("/carparts/"+ car1.getLicenseplate()+"/inspection/"+ "TIRES")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(carPartInputDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("no profile you fool"))
                .andExpect(jsonPath("checked").value(false))
        ;
    }

    public static String asJsonString(final CarPartInputDto obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}