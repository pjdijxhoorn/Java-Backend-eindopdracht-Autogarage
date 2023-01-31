package com.example.garage.Repositories;

import com.example.garage.Models.CarPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarpartRepository extends JpaRepository<CarPart, Long> {
}



