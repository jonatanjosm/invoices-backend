package com.example.invoicesbackend.repository;

import com.example.invoicesbackend.model.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InvoiceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void findByInvoiceNumber_WhenInvoiceExists_ShouldReturnInvoice() {
        // Given
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-TEST-001");
        invoice.setCustomerName("Test Customer");
        invoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        invoice.setDueDate(LocalDate.of(2023, 1, 31));
        invoice.setAmount(new BigDecimal("1000.00"));
        invoice.setDescription("Test Invoice");
        invoice.setStatus(Invoice.InvoiceStatus.PENDING);

        entityManager.persist(invoice);
        entityManager.flush();

        // When
        Optional<Invoice> found = invoiceRepository.findByInvoiceNumber("INV-TEST-001");

        // Then
        assertTrue(found.isPresent());
        assertEquals("INV-TEST-001", found.get().getInvoiceNumber());
        assertEquals("Test Customer", found.get().getCustomerName());
    }

    @Test
    void findByInvoiceNumber_WhenInvoiceDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<Invoice> found = invoiceRepository.findByInvoiceNumber("NON-EXISTENT");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void existsByInvoiceNumber_WhenInvoiceExists_ShouldReturnTrue() {
        // Given
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-TEST-002");
        invoice.setCustomerName("Test Customer");
        invoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        invoice.setDueDate(LocalDate.of(2023, 1, 31));
        invoice.setAmount(new BigDecimal("1000.00"));
        invoice.setDescription("Test Invoice");
        invoice.setStatus(Invoice.InvoiceStatus.PENDING);

        entityManager.persist(invoice);
        entityManager.flush();

        // When
        boolean exists = invoiceRepository.existsByInvoiceNumber("INV-TEST-002");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByInvoiceNumber_WhenInvoiceDoesNotExist_ShouldReturnFalse() {
        // When
        boolean exists = invoiceRepository.existsByInvoiceNumber("NON-EXISTENT");

        // Then
        assertFalse(exists);
    }
}