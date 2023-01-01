package com.example.garage.Services;

import com.example.garage.Dtos.Input.InvoiceInputDto;
import com.example.garage.Dtos.Output.InvoiceOutputDto;
import com.example.garage.Exceptions.BadRequestException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.*;
import com.example.garage.Models.CarService;
import com.example.garage.Repositories.CarServiceRepository;
import com.example.garage.Repositories.InvoiceRepository;
import com.example.garage.Repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final CarServiceRepository carServiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, UserRepository userRepository, CarServiceRepository carServiceRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.carServiceRepository = carServiceRepository;
    }

    public Iterable<InvoiceOutputDto> getAllInvoices() {
        ArrayList<InvoiceOutputDto> invoiceOutputDtos = new ArrayList<>();
        Iterable<Invoice> allinvoices = invoiceRepository.findAll();
        for (Invoice a: allinvoices){
            InvoiceOutputDto invoiceDto = transferInvoiceToDto(a);
            invoiceOutputDtos.add(invoiceDto);
        }
        return invoiceOutputDtos;
    }

    public InvoiceOutputDto getOneInvoiceByID(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isEmpty()){
            throw new RecordNotFoundException("no invoice found with id: "+ id);
        }else {
            Invoice invoice1 = invoice.get();
            return transferInvoiceToDto(invoice1);
        }
    }

    public Iterable<InvoiceOutputDto> getAllInvoicesfromUser() {
        String currentUserName = new String();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            Optional<User> currentuser = userRepository.findById(currentUserName);
            if (currentuser.isPresent()){
                User user = currentuser.get();
                ArrayList<InvoiceOutputDto> invoiceOutputDtos = new ArrayList<>();
                Iterable<Invoice> allinvoices = invoiceRepository.findByUser(user);
                for (Invoice a: allinvoices){
                    InvoiceOutputDto invoiceDto = transferInvoiceToDto(a);
                    invoiceOutputDtos.add(invoiceDto);
            }
            return invoiceOutputDtos;
            }else {
                throw new RecordNotFoundException("this users seems to have no values");
            }
        }
        throw new RecordNotFoundException("no User is logged in at the moment");
    }

    public long createInvoice(long service_id) {
        Optional<CarService> optionalcarservice = carServiceRepository.findById(service_id);
        if (optionalcarservice.isEmpty()){
            throw new RecordNotFoundException("no car service found with this id "+ service_id);
        }
        else if (!optionalcarservice.get().isMechanic_done()){
            throw new BadRequestException("The mechanic isn't yet finished with his job so you cant make the invoice just yet. ");
        }
        else {
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

            return savedInvoice.getId();}
    }

    public InvoiceOutputDto updatePayedInvoice (long id, InvoiceOutputDto invoiceOutputDto){
        Optional<Invoice> optionalinvoice = invoiceRepository.findById(id);
        if (optionalinvoice.isEmpty()){
            throw new RecordNotFoundException("no Invoice with id: " + id );
        }else {
            Invoice updatedInvoice = optionalinvoice.get();
            updatedInvoice.setPayed(invoiceOutputDto.isPayed());
            Invoice savedInvoice = invoiceRepository.save(updatedInvoice);
            return transferInvoiceToDto(savedInvoice);
        }
    }

   /* public InvoiceOutputDto updateInvoice (long id, InvoiceOutputDto invoiceOutputDto){
        Optional<Invoice> optionalinvoice = invoiceRepository.findById(id);
        if (optionalinvoice.isEmpty()){
            throw new RecordNotFoundException("no Invoice with id: " + id );
        }else {
            Invoice updatedInvoice = optionalinvoice.get();
            updatedInvoice.setPayed(invoiceOutputDto.isPayed());
            updatedInvoice.setTotalrepaircost(invoiceOutputDto.getTotalCost());
            updatedInvoice.setRepairDate(invoiceOutputDto.getRepairDate());
            updatedInvoice.setUser(invoiceOutputDto.getUser());
            return transferInvoiceToDto(updatedInvoice);
        }

    }*/

    public String deleteInvoice(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isEmpty()){
            throw new RecordNotFoundException("No Invoice found with the id of : "+ id);
        }
        else {
            Invoice invoice1 = invoice.get();
            invoiceRepository.delete(invoice1);
            return "Invoice Removed successfully";}
    }



    private InvoiceOutputDto transferInvoiceToDto(Invoice invoice) {
        InvoiceOutputDto invoiceDto= new InvoiceOutputDto();

        if (invoice.getUser() !=null){
            invoiceDto.setUser(invoice.getUser());
        }
        if (invoice.getRepairDate() !=null){
            invoiceDto.setRepairDate(invoice.getRepairDate());
        }
        invoiceDto.setPayed(invoice.isPayed());
        if (invoice.getUser() !=null){
            invoiceDto.setUser(invoice.getUser());
        }
        if (invoice.getTotalrepaircost() != 0.0){
            invoiceDto.setTotalCost(invoice.getTotalrepaircost());
        }

        return invoiceDto;
    }

    private Invoice transferDtotoInvoice(InvoiceInputDto invoiceInputDto){
        Invoice invoice = new Invoice();

        invoice.setUser(invoiceInputDto.getUser());
        invoice.setRepairDate(invoiceInputDto.getRepairDate());
        invoice.setPayed(invoiceInputDto.isPayed());
        invoice.setTotalrepaircost(invoiceInputDto.getTotalCost());


        return invoice;
    }





}
