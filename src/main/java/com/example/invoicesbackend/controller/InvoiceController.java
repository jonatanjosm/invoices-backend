package com.example.invoicesbackend.controller;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoices() {
        List<InvoiceResponseDto> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable Long id) {
        InvoiceResponseDto invoice = invoiceService.getInvoiceById(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceByInvoiceNumber(@PathVariable String invoiceNumber) {
        InvoiceResponseDto invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNumber);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InvoiceResponseDto> createInvoice(@Valid @RequestBody InvoiceRequestDto invoiceRequestDto) {
        InvoiceResponseDto newInvoice = invoiceService.createInvoice(invoiceRequestDto);
        return new ResponseEntity<>(newInvoice, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceRequestDto invoiceRequestDto) {
        InvoiceResponseDto updatedInvoice = invoiceService.updateInvoice(id, invoiceRequestDto);
        return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
    }
}
