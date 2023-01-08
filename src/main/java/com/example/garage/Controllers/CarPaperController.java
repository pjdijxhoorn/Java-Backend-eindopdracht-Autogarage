package com.example.garage.Controllers;

import com.example.garage.Services.CarpaperService;
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


    public CarPaperController(CarpaperService carpaperService) {
        this.carpaperService = carpaperService;
    }

    @GetMapping(value = "/getpdfcarpapers/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getCarpapersById(@PathVariable long id) {
        return carpaperService.getCarPapersById(id);
    }

    @PostMapping("/upload/{user_id}")
    @ResponseBody
    public ResponseEntity<String> uploadCarpapers(@PathVariable String user_id, @RequestParam(name = "pdf") MultipartFile file, @RequestParam(name = "licenseplate") String licenseplate) throws IOException {
        carpaperService.uploadCarpapers(user_id, file, licenseplate);
        return ResponseEntity.ok("Document saved!");
    }
}


