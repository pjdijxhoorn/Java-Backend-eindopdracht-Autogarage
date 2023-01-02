package com.example.garage.Repositories;

import com.example.garage.Models.CarService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarServiceRepository extends JpaRepository<CarService, Long> {
}
