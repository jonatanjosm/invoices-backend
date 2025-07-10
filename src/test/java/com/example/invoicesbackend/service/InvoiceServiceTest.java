package com.example.invoicesbackend.service;

import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice testInvoice1;
    private Invoice testInvoice2;
    private List<Invoice> allInvoices;

    @BeforeEach
    void setUp() {
        testInvoice1 = new Invoice();
        testInvoice1.setId(1L);
        testInvoice1.setInvoiceNumber("INV-001");
        testInvoice1.setCustomerName("Test Customer 1");
        testInvoice1.setInvoiceDate(LocalDate.of(2023, 1, 1));
        testInvoice1.setDueDate(LocalDate.of(2023, 1, 31));
        testInvoice1.setAmount(new BigDecimal("1000.00"));
        testInvoice1.setDescription("Test Invoice 1");
        testInvoice1.setStatus(Invoice.InvoiceStatus.PENDING);

        testInvoice2 = new Invoice();
        testInvoice2.setId(2L);
        testInvoice2.setInvoiceNumber("INV-002");
        testInvoice2.setCustomerName("Test Customer 2");
        testInvoice2.setInvoiceDate(LocalDate.of(2023, 2, 1));
        testInvoice2.setDueDate(LocalDate.of(2023, 2, 28));
        testInvoice2.setAmount(new BigDecimal("2000.00"));
        testInvoice2.setDescription("Test Invoice 2");
        testInvoice2.setStatus(Invoice.InvoiceStatus.PAID);

        allInvoices = Arrays.asList(testInvoice1, testInvoice2);
    }

    @Test
    void getAllInvoices_ShouldReturnAllInvoices() {
        when(invoiceRepository.findAll()).thenReturn(allInvoices);

        List<Invoice> result = invoiceService.getAllInvoices();

        assertEquals(2, result.size());
        assertEquals("INV-001", result.get(0).getInvoiceNumber());
        assertEquals("INV-002", result.get(1).getInvoiceNumber());
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void getInvoiceById_WhenInvoiceExists_ShouldReturnInvoice() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice1));

        Invoice result = invoiceService.getInvoiceById(1L);

        assertEquals(1L, result.getId());
        assertEquals("INV-001", result.getInvoiceNumber());
        assertEquals("Test Customer 1", result.getCustomerName());
        verify(invoiceRepository, times(1)).findById(1L);
    }

    @Test
    void getInvoiceById_WhenInvoiceDoesNotExist_ShouldThrowException() {
        when(invoiceRepository.findById(3L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            invoiceService.getInvoiceById(3L);
        });

        assertEquals("Invoice not found with id: 3", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(3L);
    }

    @Test
    void getInvoiceByInvoiceNumber_WhenInvoiceExists_ShouldReturnInvoice() {
        when(invoiceRepository.findByInvoiceNumber("INV-001")).thenReturn(Optional.of(testInvoice1));

        Invoice result = invoiceService.getInvoiceByInvoiceNumber("INV-001");

        assertEquals(1L, result.getId());
        assertEquals("INV-001", result.getInvoiceNumber());
        assertEquals("Test Customer 1", result.getCustomerName());
        verify(invoiceRepository, times(1)).findByInvoiceNumber("INV-001");
    }

    @Test
    void getInvoiceByInvoiceNumber_WhenInvoiceDoesNotExist_ShouldThrowException() {
        when(invoiceRepository.findByInvoiceNumber("INV-999")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            invoiceService.getInvoiceByInvoiceNumber("INV-999");
        });

        assertEquals("Invoice not found with invoice number: INV-999", exception.getMessage());
        verify(invoiceRepository, times(1)).findByInvoiceNumber("INV-999");
    }

    @Test
    void createInvoice_WhenInvoiceNumberDoesNotExist_ShouldCreateInvoice() {
        Invoice newInvoice = new Invoice();
        newInvoice.setInvoiceNumber("INV-003");
        newInvoice.setCustomerName("New Customer");
        newInvoice.setInvoiceDate(LocalDate.of(2023, 3, 1));
        newInvoice.setDueDate(LocalDate.of(2023, 3, 31));
        newInvoice.setAmount(new BigDecimal("3000.00"));
        newInvoice.setDescription("New Invoice");
        newInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        Invoice savedInvoice = new Invoice();
        savedInvoice.setId(3L);
        savedInvoice.setInvoiceNumber("INV-003");
        savedInvoice.setCustomerName("New Customer");
        savedInvoice.setInvoiceDate(LocalDate.of(2023, 3, 1));
        savedInvoice.setDueDate(LocalDate.of(2023, 3, 31));
        savedInvoice.setAmount(new BigDecimal("3000.00"));
        savedInvoice.setDescription("New Invoice");
        savedInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        when(invoiceRepository.existsByInvoiceNumber("INV-003")).thenReturn(false);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

        Invoice result = invoiceService.createInvoice(newInvoice);

        assertEquals(3L, result.getId());
        assertEquals("INV-003", result.getInvoiceNumber());
        assertEquals("New Customer", result.getCustomerName());
        verify(invoiceRepository, times(1)).existsByInvoiceNumber("INV-003");
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void createInvoice_WhenInvoiceNumberExists_ShouldThrowException() {
        Invoice newInvoice = new Invoice();
        newInvoice.setInvoiceNumber("INV-001");
        newInvoice.setCustomerName("New Customer");
        newInvoice.setInvoiceDate(LocalDate.of(2023, 3, 1));
        newInvoice.setDueDate(LocalDate.of(2023, 3, 31));
        newInvoice.setAmount(new BigDecimal("3000.00"));
        newInvoice.setDescription("New Invoice");
        newInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        when(invoiceRepository.existsByInvoiceNumber("INV-001")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(newInvoice);
        });

        assertEquals("Invoice with number INV-001 already exists", exception.getMessage());
        verify(invoiceRepository, times(1)).existsByInvoiceNumber("INV-001");
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    void updateInvoice_WhenInvoiceExistsAndInvoiceNumberNotChanged_ShouldUpdateInvoice() {
        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setInvoiceNumber("INV-001");
        updatedInvoice.setCustomerName("Updated Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice1));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(updatedInvoice);

        Invoice result = invoiceService.updateInvoice(1L, updatedInvoice);

        assertEquals("INV-001", result.getInvoiceNumber());
        assertEquals("Updated Customer", result.getCustomerName());
        assertEquals(new BigDecimal("1500.00"), result.getAmount());
        assertEquals(Invoice.InvoiceStatus.PAID, result.getStatus());
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void updateInvoice_WhenInvoiceExistsAndInvoiceNumberChangedToNonExisting_ShouldUpdateInvoice() {
        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setInvoiceNumber("INV-003");
        updatedInvoice.setCustomerName("Updated Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice1));
        when(invoiceRepository.existsByInvoiceNumber("INV-003")).thenReturn(false);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(updatedInvoice);

        Invoice result = invoiceService.updateInvoice(1L, updatedInvoice);

        assertEquals("INV-003", result.getInvoiceNumber());
        assertEquals("Updated Customer", result.getCustomerName());
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).existsByInvoiceNumber("INV-003");
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void updateInvoice_WhenInvoiceExistsAndInvoiceNumberChangedToExisting_ShouldThrowException() {
        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setInvoiceNumber("INV-002");
        updatedInvoice.setCustomerName("Updated Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice1));
        when(invoiceRepository.existsByInvoiceNumber("INV-002")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.updateInvoice(1L, updatedInvoice);
        });

        assertEquals("Invoice with number INV-002 already exists", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).existsByInvoiceNumber("INV-002");
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }
}