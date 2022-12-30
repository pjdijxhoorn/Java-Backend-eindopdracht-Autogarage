package com.example.garage.Repositories;

import com.example.garage.Models.Invoice;
import com.example.garage.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUser(User user);

}
