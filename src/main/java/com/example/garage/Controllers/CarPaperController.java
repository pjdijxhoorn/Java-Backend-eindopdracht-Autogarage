package com.example.garage.Controllers;

import com.example.garage.Services.CarpaperService;
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

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam(name = "pdf") MultipartFile file, @RequestParam(name = "licenseplate") String licenseplate) throws IOException {
            carpaperService.saveDocument(file, licenseplate);
            return "Document saved!";
        }
    }


