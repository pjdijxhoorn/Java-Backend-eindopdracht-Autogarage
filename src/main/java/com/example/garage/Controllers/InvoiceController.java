package com.example.garage.Controllers;

import com.example.garage.Dtos.Input.CarInputDto;
import com.example.garage.Dtos.Input.InvoiceInputDto;
import com.example.garage.Dtos.Output.CarOutputDto;
import com.example.garage.Dtos.Output.InvoiceOutputDto;
import com.example.garage.Services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

import static com.example.garage.Utilities.Utilities.getErrorString;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @GetMapping("")
    public ResponseEntity<Iterable<InvoiceOutputDto>> getAllInvoices(){
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("{id}")
    public ResponseEntity<InvoiceOutputDto> getOneInvoiceByID(@PathVariable long id) {
        return ResponseEntity.ok(invoiceService.getOneInvoiceByID(id));
    }

    @GetMapping("/user")
    public ResponseEntity<Iterable<InvoiceOutputDto>> getAllInvoicesfromUser(){
        return ResponseEntity.ok(invoiceService.getAllInvoicesfromUser());
    }

    @PostMapping("")
    public ResponseEntity<String> createInvoice(@Valid @RequestBody InvoiceInputDto invoiceInputDto, BindingResult br){
        if (br.hasErrors()) {
            String errorString = getErrorString(br);
            return new ResponseEntity<>(errorString, HttpStatus.BAD_REQUEST);

        } else {
            long createdId = invoiceService.createInvoice(invoiceInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/car/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Invoice created");
        }
    }

    @PutMapping("{id}/payed")
    public ResponseEntity<InvoiceOutputDto> updatePayedInvoice(@PathVariable long id, @RequestBody InvoiceOutputDto invoiceOutputDto){
        return ResponseEntity.ok(invoiceService.updatePayedInvoice(id, invoiceOutputDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<InvoiceOutputDto> updateInvoice(@PathVariable long id, @RequestBody InvoiceOutputDto invoiceOutputDto){
        return ResponseEntity.ok(invoiceService.updateInvoice(id, invoiceOutputDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable long id) {
        return ResponseEntity.ok(invoiceService.deleteInvoice(id));
    }
}
