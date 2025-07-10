package com.example.invoicesbackend.service;

import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));
    }

    public Invoice getInvoiceByInvoiceNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with invoice number: " + invoiceNumber));
    }

    public Invoice createInvoice(Invoice invoice) {
        if (invoiceRepository.existsByInvoiceNumber(invoice.getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + invoice.getInvoiceNumber() + " already exists");
        }
        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Invoice invoice = getInvoiceById(id);
        
        // Only update if the invoice number is not changed or the new invoice number doesn't exist
        if (!invoice.getInvoiceNumber().equals(invoiceDetails.getInvoiceNumber()) && 
            invoiceRepository.existsByInvoiceNumber(invoiceDetails.getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + invoiceDetails.getInvoiceNumber() + " already exists");
        }
        
        invoice.setInvoiceNumber(invoiceDetails.getInvoiceNumber());
        invoice.setCustomerName(invoiceDetails.getCustomerName());
        invoice.setInvoiceDate(invoiceDetails.getInvoiceDate());
        invoice.setDueDate(invoiceDetails.getDueDate());
        invoice.setAmount(invoiceDetails.getAmount());
        invoice.setDescription(invoiceDetails.getDescription());
        invoice.setStatus(invoiceDetails.getStatus());
        
        return invoiceRepository.save(invoice);
    }
}