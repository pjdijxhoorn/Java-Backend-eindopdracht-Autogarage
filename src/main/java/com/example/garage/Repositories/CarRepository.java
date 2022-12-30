package com.example.garage.Repositories;

import com.example.garage.Models.Car;
import com.example.garage.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findBylicenseplate(String licenseplate);
    List<Car> findByUser(User user);
}
