package com.example.garage.Repositories;

import com.example.garage.Models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
     Car findBylicenseplate(String licenseplate);
}
