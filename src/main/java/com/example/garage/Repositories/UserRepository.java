package com.example.garage.Repositories;

import com.example.garage.Models.Car;
import com.example.garage.Models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
}
