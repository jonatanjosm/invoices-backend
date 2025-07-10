package com.example.invoicesbackend.controller;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;

    private InvoiceResponseDto testInvoice1;
    private InvoiceResponseDto testInvoice2;
    private List<InvoiceResponseDto> allInvoices;

    @BeforeEach
    void setUp() {
        testInvoice1 = new InvoiceResponseDto();
        testInvoice1.setId(1L);
        testInvoice1.setInvoiceNumber("INV-001");
        testInvoice1.setCustomerName("Test Customer 1");
        testInvoice1.setInvoiceDate(LocalDate.of(2023, 1, 1));
        testInvoice1.setDueDate(LocalDate.of(2023, 1, 31));
        testInvoice1.setAmount(new BigDecimal("1000.00"));
        testInvoice1.setDescription("Test Invoice 1");
        testInvoice1.setStatus(Invoice.InvoiceStatus.PENDING);

        testInvoice2 = new InvoiceResponseDto();
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
    void getAllInvoices_ShouldReturnAllInvoices() throws Exception {
        when(invoiceService.getAllInvoices()).thenReturn(allInvoices);

        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].invoiceNumber", is("INV-001")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].invoiceNumber", is("INV-002")));
    }

    @Test
    void getInvoiceById_ShouldReturnInvoice() throws Exception {
        when(invoiceService.getInvoiceById(1L)).thenReturn(testInvoice1);

        mockMvc.perform(get("/api/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.invoiceNumber", is("INV-001")))
                .andExpect(jsonPath("$.customerName", is("Test Customer 1")));
    }

    @Test
    void getInvoiceByInvoiceNumber_ShouldReturnInvoice() throws Exception {
        when(invoiceService.getInvoiceByInvoiceNumber("INV-001")).thenReturn(testInvoice1);

        mockMvc.perform(get("/api/invoices/number/INV-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.invoiceNumber", is("INV-001")))
                .andExpect(jsonPath("$.customerName", is("Test Customer 1")));
    }

    @Test
    void createInvoice_ShouldCreateAndReturnInvoice() throws Exception {
        InvoiceRequestDto newInvoice = new InvoiceRequestDto();
        newInvoice.setInvoiceNumber("INV-003");
        newInvoice.setCustomerName("New Customer");
        newInvoice.setInvoiceDate(LocalDate.of(2023, 3, 1));
        newInvoice.setDueDate(LocalDate.of(2023, 3, 31));
        newInvoice.setAmount(new BigDecimal("3000.00"));
        newInvoice.setDescription("New Invoice");
        newInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        InvoiceResponseDto savedInvoice = new InvoiceResponseDto();
        savedInvoice.setId(3L);
        savedInvoice.setInvoiceNumber("INV-003");
        savedInvoice.setCustomerName("New Customer");
        savedInvoice.setInvoiceDate(LocalDate.of(2023, 3, 1));
        savedInvoice.setDueDate(LocalDate.of(2023, 3, 31));
        savedInvoice.setAmount(new BigDecimal("3000.00"));
        savedInvoice.setDescription("New Invoice");
        savedInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        when(invoiceService.createInvoice(any(InvoiceRequestDto.class))).thenReturn(savedInvoice);

        mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newInvoice)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.invoiceNumber", is("INV-003")))
                .andExpect(jsonPath("$.customerName", is("New Customer")));
    }

    @Test
    void updateInvoice_ShouldUpdateAndReturnInvoice() throws Exception {
        InvoiceRequestDto updatedInvoice = new InvoiceRequestDto();
        updatedInvoice.setInvoiceNumber("INV-001");
        updatedInvoice.setCustomerName("Updated Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        InvoiceResponseDto savedInvoice = new InvoiceResponseDto();
        savedInvoice.setId(1L);
        savedInvoice.setInvoiceNumber("INV-001");
        savedInvoice.setCustomerName("Updated Customer");
        savedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        savedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        savedInvoice.setAmount(new BigDecimal("1500.00"));
        savedInvoice.setDescription("Updated Invoice");
        savedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        when(invoiceService.updateInvoice(eq(1L), any(InvoiceRequestDto.class))).thenReturn(savedInvoice);

        mockMvc.perform(put("/api/invoices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInvoice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.invoiceNumber", is("INV-001")))
                .andExpect(jsonPath("$.customerName", is("Updated Customer")))
                .andExpect(jsonPath("$.amount", is(1500.00)))
                .andExpect(jsonPath("$.status", is("PAID")));
    }
}
