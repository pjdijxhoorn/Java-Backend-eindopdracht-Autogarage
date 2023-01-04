package com.example.garage.Controllers;

import com.example.garage.Dtos.Output.InvoiceOutputDto;
import com.example.garage.Services.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @GetMapping("/pdf/generate/{id}")
    public void generatePDF(@PathVariable long id, HttpServletResponse response) throws IOException, MessagingException {

        response.setContentType("applcation/pdf");
        DateFormat dateformatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateTime = dateformatter.format(new Date());

        String headerKey = "content-Disposition";
        String headerValue = "attachment; filename="+ id+"Invoice_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

      /*  this.invoiceService.export(id,response);
        invoice.setPdfInvoice(this.invoiceService.export);
*/
    }

    @PostMapping("{service_id}")
    public ResponseEntity<String> createInvoice(@PathVariable long service_id){
            long createdId = invoiceService.createInvoice(service_id);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/Invoice/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Invoice created");
        }


    @PutMapping("{id}/payed")
    public ResponseEntity<InvoiceOutputDto> updatePayedInvoice(@PathVariable long id, @RequestBody InvoiceOutputDto invoiceOutputDto){
        return ResponseEntity.ok(invoiceService.updatePayedInvoice(id, invoiceOutputDto));
    }

    /*@PutMapping("{id}")
    public ResponseEntity<InvoiceOutputDto> updateInvoice(@PathVariable long id, @RequestBody InvoiceOutputDto invoiceOutputDto){
        return ResponseEntity.ok(invoiceService.updateInvoice(id, invoiceOutputDto));
    }*/

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable long id) {
        return ResponseEntity.ok(invoiceService.deleteInvoice(id));
    }
}
