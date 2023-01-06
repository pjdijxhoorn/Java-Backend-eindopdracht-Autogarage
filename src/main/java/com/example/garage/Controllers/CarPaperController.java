package com.example.garage.Controllers;

import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.CarPaper;
import com.example.garage.Repositories.CarPaperRepository;
import com.example.garage.Services.CarpaperService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/carpapers")

public class CarPaperController {

    private final CarpaperService carpaperService;
    private final CarPaperRepository carPaperRepository;

    public CarPaperController(CarpaperService carpaperService, CarPaperRepository carPaperRepository) {
        this.carpaperService = carpaperService;
        this.carPaperRepository = carPaperRepository;
    }

    @GetMapping(value = "/{id}/getpdfcarpapers", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getCarpapersById(@PathVariable long id) {
        return carpaperService.getCarPapersById(id);
    }

    @PostMapping("/upload/{user_id}")
    @ResponseBody
    public ResponseEntity<String> uploadCarpapers(@PathVariable String user_id, @RequestParam(name = "pdf") MultipartFile file, @RequestParam(name = "licenseplate") String licenseplate) throws IOException {
            carpaperService.uploadCarpapers(user_id, file, licenseplate);
            return ResponseEntity.ok("Document saved!") ;
        }
    }


