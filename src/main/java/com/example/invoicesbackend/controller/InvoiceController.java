package com.example.invoicesbackend.controller;

import com.example.invoicesbackend.model.Invoice;
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
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<Invoice> getInvoiceByInvoiceNumber(@PathVariable String invoiceNumber) {
        Invoice invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNumber);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        Invoice newInvoice = invoiceService.createInvoice(invoice);
        return new ResponseEntity<>(newInvoice, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @Valid @RequestBody Invoice invoiceDetails) {
        Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDetails);
        return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
    }
}