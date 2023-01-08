package com.example.garage.Services;

import com.example.garage.Dtos.Input.InvoiceInputDto;
import com.example.garage.Dtos.Output.InvoiceOutputDto;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.CarService;
import com.example.garage.Models.*;
import com.example.garage.Repositories.CarServiceRepository;
import com.example.garage.Repositories.InvoiceRepository;
import com.example.garage.Repositories.UserRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import static com.lowagie.text.Element.ALIGN_LEFT;
import static com.lowagie.text.Element.ALIGN_RIGHT;


@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final CarServiceRepository carServiceRepository;
    private final EmailService emailService;

    public InvoiceService(InvoiceRepository invoiceRepository, UserRepository userRepository, CarServiceRepository carServiceRepository, EmailService emailService) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.carServiceRepository = carServiceRepository;
        this.emailService = emailService;
    }

    public Iterable<InvoiceOutputDto> getAllInvoices() {
        ArrayList<InvoiceOutputDto> invoiceOutputDtos = new ArrayList<>();
        Iterable<Invoice> allinvoices = invoiceRepository.findAll();
        for (Invoice a : allinvoices) {
            InvoiceOutputDto invoiceDto = transferInvoiceToDto(a);
            invoiceOutputDtos.add(invoiceDto);
        }
        return invoiceOutputDtos;
    }

    public InvoiceOutputDto getOneInvoiceByID(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isEmpty()) {
            throw new RecordNotFoundException("no invoice found with id: " + id);
        } else {
            Invoice invoice1 = invoice.get();
            return transferInvoiceToDto(invoice1);
        }
    }

    public Iterable<InvoiceOutputDto> getAllInvoicesfromUser() {
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            if (currentuser.isPresent()) {
                User user = currentuser.get();
                ArrayList<InvoiceOutputDto> invoiceOutputDtos = new ArrayList<>();
                Iterable<Invoice> allinvoices = invoiceRepository.findByUser(user);
                for (Invoice a : allinvoices) {
                    InvoiceOutputDto invoiceDto = transferInvoiceToDto(a);
                    invoiceOutputDtos.add(invoiceDto);
                }
                return invoiceOutputDtos;
            } else {
                throw new RecordNotFoundException("this users seems to have no values");
            }
        }
        throw new RecordNotFoundException("no User is logged in at the moment");
    }

    public ResponseEntity<byte[]> getpdfinvoice(long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No car invoice found with id: " + id));
        byte[] invoicepdf = invoice.getPdfinvoice();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice" + id + ".pdf");
        headers.setContentLength(invoicepdf.length);
        return new ResponseEntity<>(invoicepdf, headers, HttpStatus.OK);
    }

    public String generateInvoicePdf(long id) throws IOException {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("no invoice found with id " + id));

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();
        //used font styles
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(22);
        Font fontparagraphinfo = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontparagraphinfo.setSize(8);
        Font fontparagraph = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontparagraph.setSize(10);
        Font lines = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        lines.setSize(22);

        // actual text on the pdf

        Image PNG = Image.getInstance("src/main/resources/garage_logomini.png");
        PNG.setAlignment(ALIGN_RIGHT);
        document.add(PNG);

        // adress info
        Paragraph paragraph = new Paragraph("GARAGE TRANSPARANT\n See what is happening to your car.\n Paleis Noordeinde\n 2500 GK Den Haag\n Phone: 030-12345678 \n Email: SomeFake@Adres.com", fontparagraphinfo);
        paragraph.setAlignment(ALIGN_LEFT);

        //title
        Paragraph paragraph1 = new Paragraph("INVOICE\n", fontTitle);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);

        //Invoice info
        Paragraph paragraph2 = new Paragraph("Costumer: " + invoice.getUser().getUsername() + "\t" + "\t" + "\t" +
                "Date: " + invoice.getRepairDate() + "\t" + "\t" + "\t" +
                "Invoice Number: " + invoice.getId() + "\t" + "\t" + "\t" +
                "Licenseplate: " + invoice.getCar().getLicenseplate(), fontparagraph);
        paragraph2.setAlignment(Paragraph.ALIGN_CENTER);


        Paragraph paragraph3 = new Paragraph("-----------------------------------------------------------------------", lines);
        paragraph3.setAlignment(Paragraph.ALIGN_TOP);

        Paragraph paragraph4 = new Paragraph(repairItemStringBuilder(invoice), fontparagraph);
        paragraph4.setAlignment(ALIGN_LEFT);


        Paragraph paragraph5 = new Paragraph("-----------------------------------------------------------------------", lines);
        paragraph5.setAlignment(Paragraph.ALIGN_TOP);

        Paragraph paragraph6 = new Paragraph(
                "Total repair cost: " + invoice.getTotalrepaircost() +
                        "\n" + "APK: " + Invoice.APKCHECK +
                        "\n" + "Total cost without tax: " + (invoice.getTotalrepaircost() + Invoice.APKCHECK) +
                        "\n" + "Tax: " + Invoice.btw + " %" +
                        "\n" + "Total cost after tax: " + invoice.getTotalcost(), fontparagraph);
        paragraph6.setAlignment(Paragraph.ALIGN_RIGHT);

        document.add(paragraph);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.close();

        PdfWriter.getInstance(document, baos).close();
        String filename = invoice.getUser().getUsername() + id + "Invoice.pdf";
        byte[] pdfinvoice = baos.toByteArray();
        invoice.setPdfinvoice(pdfinvoice);
        invoiceRepository.save(invoice);
        return filename + " made and stored to the database";
    }

    public String sendInvoicePdf(long id) throws IOException {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("no invoice found with id " + id));
        byte[] pdf;
        if (invoice.getPdfinvoice() != null) {
            pdf = invoice.getPdfinvoice();
            // I didn't seem to be able to send a file retrieved from the database without saving it to a file locally first.
            // I am not sure if this solution is good code, so I would love feedback on this.
            Path path = Paths.get("src/main/resources/Invoices/invoice" + invoice.getUser().getUsername() + ".pdf");
            Files.write(path, pdf);
            String location = String.valueOf(path);
            String emailadress = invoice.getUser().getEmail();
            Email InvoiceEmail = new Email(emailadress, "Hereby we wish to present you with the bill!", "Invoice", location);
            this.emailService.sendMailWithAttachment(InvoiceEmail);
            Path removepath = Paths.get(location);
            // Here I delete the file from local storage again.
            Files.delete(removepath);
            return "email send to user";
        } else {
            throw new RecordNotFoundException("there is now pdf found in the invoice class");
        }
    }


    public long createInvoice(long service_id) {
        Optional<CarService> optionalcarservice = carServiceRepository.findById(service_id);
        if (optionalcarservice.isEmpty()) {
            throw new RecordNotFoundException("no car service found with this id " + service_id);
        } else if (!optionalcarservice.get().isMechanic_done()) {
            throw new BadRequestException("The mechanic isn't yet finished with his job so you cant make the invoice just yet. ");
        } else {
            CarService carService = optionalcarservice.get();
            Invoice newInvoice = new Invoice();
            newInvoice.setCarService(carService);
            newInvoice.setPayed(false);
            newInvoice.setUser(carService.getCar().getUser());
            newInvoice.setRepairDate(java.time.LocalDate.now());
            newInvoice.setCar(carService.getCar());
            newInvoice.setTotalrepaircost(newInvoice.calculateRepairCost());
            newInvoice.setTotalcost(newInvoice.calculateTotalCost());

            Invoice savedInvoice = invoiceRepository.save(newInvoice);

            return savedInvoice.getId();
        }
    }

    public InvoiceOutputDto updatePayedInvoice(long id, InvoiceOutputDto invoiceOutputDto) {
        Optional<Invoice> optionalinvoice = invoiceRepository.findById(id);
        if (optionalinvoice.isEmpty()) {
            throw new RecordNotFoundException("no Invoice with id: " + id);
        } else {
            Invoice updatedInvoice = optionalinvoice.get();
            updatedInvoice.setPayed(invoiceOutputDto.isPayed());
            Invoice savedInvoice = invoiceRepository.save(updatedInvoice);
            return transferInvoiceToDto(savedInvoice);
        }
    }

    public String deleteInvoice(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isEmpty()) {
            throw new RecordNotFoundException("No Invoice found with the id of : " + id);
        } else {
            Invoice invoice1 = invoice.get();
            invoiceRepository.delete(invoice1);
            return "Invoice Removed successfully";
        }
    }


    private InvoiceOutputDto transferInvoiceToDto(Invoice invoice) {
        InvoiceOutputDto invoiceDto = new InvoiceOutputDto();

        if (invoice.getUser() != null) {
            invoiceDto.setUser(invoice.getUser());
        }
        if (invoice.getRepairDate() != null) {
            invoiceDto.setRepairDate(invoice.getRepairDate());
        }
        invoiceDto.setPayed(invoice.isPayed());
        if (invoice.getUser() != null) {
            invoiceDto.setUser(invoice.getUser());
        }
        if (invoice.getTotalrepaircost() != 0.0) {
            invoiceDto.setTotalCost(invoice.getTotalrepaircost());
        }

        return invoiceDto;
    }

    private Invoice transferDtotoInvoice(InvoiceInputDto invoiceInputDto) {
        Invoice invoice = new Invoice();

        invoice.setUser(invoiceInputDto.getUser());
        invoice.setRepairDate(invoiceInputDto.getRepairDate());
        invoice.setPayed(invoiceInputDto.isPayed());
        invoice.setTotalrepaircost(invoiceInputDto.getTotalCost());

        return invoice;
    }

    public String repairItemStringBuilder(Invoice invoice) {
        StringBuilder repairitems = new StringBuilder();
        for (Repair repair : invoice.getCarService().getRepairs()) {
            repairitems.append("Carpart: ").append(repair.getCarpart().carpartname).append("\t\t\t").append("Repair-cost: ").append(repair.getRepairCost()).append("\t\t\t").append("Repair-done: ").append(repair.isRepair_done()).append(" \n").append("Notes: ").append(repair.getNotes()).append("\n \n");
        }
        repairitems.append("APK CHECK \t\t\t" + Invoice.APKCHECK + "\t\t\tvoldaan");
        return repairitems.toString();
    }
}
