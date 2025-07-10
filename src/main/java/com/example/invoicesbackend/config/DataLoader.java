package com.example.invoicesbackend.config;

import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public DataLoader(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void run(String... args) {
        // Load sample data only if the database is empty
        if (invoiceRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        // Create sample invoices
        Invoice invoice1 = new Invoice();
        invoice1.setInvoiceNumber("INV-001");
        invoice1.setCustomerName("Acme Corporation");
        invoice1.setInvoiceDate(LocalDate.now().minusDays(30));
        invoice1.setAmount(new BigDecimal("1250.00"));
        invoice1.setDescription("Consulting services - Q1");
        invoice1.setStatus(Invoice.InvoiceStatus.PENDING);

        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceNumber("INV-002");
        invoice2.setCustomerName("Globex Inc.");
        invoice2.setInvoiceDate(LocalDate.now().minusDays(45));
        invoice2.setAmount(new BigDecimal("2750.50"));
        invoice2.setDescription("Software development - Phase 1");
        invoice2.setStatus(Invoice.InvoiceStatus.PAID);

        Invoice invoice3 = new Invoice();
        invoice3.setInvoiceNumber("INV-003");
        invoice3.setCustomerName("Wayne Enterprises");
        invoice3.setInvoiceDate(LocalDate.now().minusDays(60));
        invoice3.setAmount(new BigDecimal("4500.75"));
        invoice3.setDescription("Hardware supplies");
        invoice3.setStatus(Invoice.InvoiceStatus.PENDING);

        // Save to database
        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);
        invoiceRepository.save(invoice3);

        System.out.println("Sample data loaded successfully!");
    }
}
