package com.example.garage.Services;


import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.CarPaper;
import com.example.garage.Models.User;
import com.example.garage.Repositories.CarPaperRepository;
import com.example.garage.Repositories.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.garage.Utilities.licenseplateValidator.validateLicensePlate;

@Service
public class CarpaperService {

    private final CarPaperRepository carPaperRepository;
    private final UserRepository userRepository;

    public CarpaperService(CarPaperRepository carPaperRepository, UserRepository userRepository) {
        this.carPaperRepository = carPaperRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<byte[]> getCarPapersById(String id) {
        CarPaper carPaper = carPaperRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No car papers found with id: " + id));
        byte[] carPapers = carPaper.getCarPapers();
        if (carPapers == null){
            throw new RecordNotFoundException("there is no carpaper pdf yet.");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "carpapers" + id + ".pdf");
        headers.setContentLength(carPapers.length);
        return new ResponseEntity<>(carPapers, headers, HttpStatus.OK);
    }

    public void uploadCarpapers(String user_id, MultipartFile file, String licenseplate) throws IOException {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RecordNotFoundException("no user found with id " + user_id));
        if (!validateLicensePlate(licenseplate))
            throw new RecordNotFoundException("This is not a valid Dutch license plate");
        CarPaper testcarPaper = carPaperRepository.findBylicenseplate(licenseplate);
        if (testcarPaper != null) {
            throw new BadRequestException("this licenseplate is already registerd.");
        } else {
            CarPaper carPaper = new CarPaper();
            carPaper.setLicenseplate(licenseplate);
            carPaper.setUser(user);
            carPaper.carPapers = file.getBytes();
            carPaperRepository.save(carPaper);
        }
    }
}
