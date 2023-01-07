package com.example.garage.Controllers;

import com.example.garage.Dtos.Output.InvoiceOutputDto;
import com.example.garage.Services.InvoiceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URI;


@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<InvoiceOutputDto>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("{id}")
    public ResponseEntity<InvoiceOutputDto> getOneInvoiceByID(@PathVariable long id) {
        return ResponseEntity.ok(invoiceService.getOneInvoiceByID(id));
    }

    @GetMapping("/user")
    public ResponseEntity<Iterable<InvoiceOutputDto>> getAllInvoicesfromUser() {
        return ResponseEntity.ok(invoiceService.getAllInvoicesfromUser());
    }

    @GetMapping(value = "/{id}/getpdfinvoice", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getgetpdfinvoice(@PathVariable long id) {
        return invoiceService.getpdfinvoice(id);
    }

    @PutMapping("{id}/generateInvoicePdf")
    public ResponseEntity<String> generateInvoicePdf(@PathVariable long id) throws IOException {
        return ResponseEntity.ok(invoiceService.generateInvoicePdf(id));
    }

    @PutMapping("{id}/sendinvoice")
    public ResponseEntity<String> sendInvoicePdf(@PathVariable long id) throws MessagingException, IOException {
        return ResponseEntity.ok(invoiceService.sendInvoicePdf(id));
    }

    @PostMapping("{service_id}")
    public ResponseEntity<String> createInvoice(@PathVariable long service_id) {
        long createdId = invoiceService.createInvoice(service_id);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/Invoice/" + createdId).toUriString());
        return ResponseEntity.created(uri).body("Invoice created");
    }

    @PutMapping("{id}/payed")
    public ResponseEntity<InvoiceOutputDto> updatePayedInvoice(@PathVariable long id, @RequestBody InvoiceOutputDto invoiceOutputDto) {
        return ResponseEntity.ok(invoiceService.updatePayedInvoice(id, invoiceOutputDto));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable long id) {
        return ResponseEntity.ok(invoiceService.deleteInvoice(id));
    }
}
