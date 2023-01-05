package com.example.garage.Repositories;

import com.example.garage.Models.CarPaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarPaperRepository extends JpaRepository<CarPaper, Long> {
    CarPaper findBylicenseplate(String licenseplate);
}
