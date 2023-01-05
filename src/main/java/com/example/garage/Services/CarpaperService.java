package com.example.garage.Services;


import antlr.StringUtils;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Models.Car;
import com.example.garage.Models.CarPaper;
import com.example.garage.Repositories.CarPaperRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CarpaperService {

    private final CarPaperRepository carPaperRepository;

    public CarpaperService(CarPaperRepository carPaperRepository) {
        this.carPaperRepository = carPaperRepository;
    }

    public void saveDocument(MultipartFile file, String licenseplate) throws IOException {
        CarPaper testcarPaper = carPaperRepository.findBylicenseplate(licenseplate);
        if(testcarPaper != null){
            throw new BadRequestException("this licenseplate is already registerd.");
        }
        else {
            CarPaper carPaper = new CarPaper();
            carPaper.setLicenseplate(licenseplate);
            carPaper.carPapers = file.getBytes();
            carPaperRepository.save(carPaper);
        }
    }





}
