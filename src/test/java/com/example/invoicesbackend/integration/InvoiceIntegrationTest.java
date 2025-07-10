package com.example.invoicesbackend.integration;

import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.repository.InvoiceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InvoiceRepository invoiceRepository;

    private Invoice testInvoice;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        invoiceRepository.deleteAll();

        // Create a test invoice
        testInvoice = new Invoice();
        testInvoice.setInvoiceNumber("INT-TEST-001");
        testInvoice.setCustomerName("Integration Test Customer");
        testInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        testInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        testInvoice.setAmount(new BigDecimal("1000.00"));
        testInvoice.setDescription("Integration Test Invoice");
        testInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        invoiceRepository.save(testInvoice);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        invoiceRepository.deleteAll();
    }

    @Test
    void getAllInvoices_ShouldReturnAllInvoices() throws Exception {
        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].invoiceNumber", is("INT-TEST-001")))
                .andExpect(jsonPath("$[0].customerName", is("Integration Test Customer")));
    }

    @Test
    void getInvoiceById_WhenInvoiceExists_ShouldReturnInvoice() throws Exception {
        Long id = testInvoice.getId();

        mockMvc.perform(get("/api/invoices/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.invoiceNumber", is("INT-TEST-001")))
                .andExpect(jsonPath("$.customerName", is("Integration Test Customer")));
    }

    @Test
    void getInvoiceById_WhenInvoiceDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/invoices/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getInvoiceByInvoiceNumber_WhenInvoiceExists_ShouldReturnInvoice() throws Exception {
        mockMvc.perform(get("/api/invoices/number/{invoiceNumber}", "INT-TEST-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.invoiceNumber", is("INT-TEST-001")))
                .andExpect(jsonPath("$.customerName", is("Integration Test Customer")));
    }

    @Test
    void getInvoiceByInvoiceNumber_WhenInvoiceDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/invoices/number/NON-EXISTENT"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createInvoice_WhenInvoiceNumberDoesNotExist_ShouldCreateAndReturnInvoice() throws Exception {
        Invoice newInvoice = new Invoice();
        newInvoice.setInvoiceNumber("INT-TEST-002");
        newInvoice.setCustomerName("New Integration Test Customer");
        newInvoice.setInvoiceDate(LocalDate.of(2023, 2, 1));
        newInvoice.setDueDate(LocalDate.of(2023, 2, 28));
        newInvoice.setAmount(new BigDecimal("2000.00"));
        newInvoice.setDescription("New Integration Test Invoice");
        newInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newInvoice)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.invoiceNumber", is("INT-TEST-002")))
                .andExpect(jsonPath("$.customerName", is("New Integration Test Customer")));
    }

    @Test
    void createInvoice_WhenInvoiceNumberExists_ShouldReturnBadRequest() throws Exception {
        Invoice newInvoice = new Invoice();
        newInvoice.setInvoiceNumber("INT-TEST-001"); // Already exists
        newInvoice.setCustomerName("New Integration Test Customer");
        newInvoice.setInvoiceDate(LocalDate.of(2023, 2, 1));
        newInvoice.setDueDate(LocalDate.of(2023, 2, 28));
        newInvoice.setAmount(new BigDecimal("2000.00"));
        newInvoice.setDescription("New Integration Test Invoice");
        newInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newInvoice)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateInvoice_WhenInvoiceExistsAndInvoiceNumberNotChanged_ShouldUpdateAndReturnInvoice() throws Exception {
        Long id = testInvoice.getId();

        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setInvoiceNumber("INT-TEST-001"); // Same as existing
        updatedInvoice.setCustomerName("Updated Integration Test Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Integration Test Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        mockMvc.perform(put("/api/invoices/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInvoice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.invoiceNumber", is("INT-TEST-001")))
                .andExpect(jsonPath("$.customerName", is("Updated Integration Test Customer")))
                .andExpect(jsonPath("$.amount", is(1500.00)))
                .andExpect(jsonPath("$.status", is("PAID")));
    }

    @Test
    void updateInvoice_WhenInvoiceExistsAndInvoiceNumberChangedToNonExisting_ShouldUpdateAndReturnInvoice() throws Exception {
        Long id = testInvoice.getId();

        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setInvoiceNumber("INT-TEST-003"); // New invoice number
        updatedInvoice.setCustomerName("Updated Integration Test Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Integration Test Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        mockMvc.perform(put("/api/invoices/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInvoice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.invoiceNumber", is("INT-TEST-003")))
                .andExpect(jsonPath("$.customerName", is("Updated Integration Test Customer")));
    }

    @Test
    void updateInvoice_WhenInvoiceDoesNotExist_ShouldReturnNotFound() throws Exception {
        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setInvoiceNumber("INT-TEST-003");
        updatedInvoice.setCustomerName("Updated Integration Test Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Integration Test Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        mockMvc.perform(put("/api/invoices/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInvoice)))
                .andExpect(status().isNotFound());
    }
}