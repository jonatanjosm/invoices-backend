package com.example.invoicesbackend.controller;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.request.LineItemRequestDto;
import com.example.invoicesbackend.dto.request.PaymentRequestDto;
import com.example.invoicesbackend.dto.request.UpdateInvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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

    @Test
    public void testGetAllInvoices_Success() throws Exception {
        // Prepare test data
        InvoiceResponseDto invoice1 = new InvoiceResponseDto();
        invoice1.setId(1L);
        invoice1.setInvoiceNumber("INV-001");
        invoice1.setCustomerName("Test Customer 1");
        invoice1.setInvoiceDate(LocalDate.now());
        invoice1.setAmount(new BigDecimal("100.00"));
        invoice1.setStatus(Invoice.InvoiceStatus.PENDING);

        InvoiceResponseDto invoice2 = new InvoiceResponseDto();
        invoice2.setId(2L);
        invoice2.setInvoiceNumber("INV-002");
        invoice2.setCustomerName("Test Customer 2");
        invoice2.setInvoiceDate(LocalDate.now());
        invoice2.setAmount(new BigDecimal("200.00"));
        invoice2.setStatus(Invoice.InvoiceStatus.PENDING);

        List<InvoiceResponseDto> invoices = Arrays.asList(invoice1, invoice2);

        // Mock service method
        when(invoiceService.getAllInvoices()).thenReturn(invoices);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].invoiceNumber").value("INV-001"))
                .andExpect(jsonPath("$[1].invoiceNumber").value("INV-002"));
    }

    @Test
    public void testGetAllInvoices_Error() throws Exception {
        // Mock service method to throw exception
        when(invoiceService.getAllInvoices()).thenThrow(new RuntimeException("Database connection error"));

        // Perform GET request and validate response
        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Database connection error"));
    }

    @Test
    public void testGetInvoiceByInvoiceNumber_Success() throws Exception {
        // Prepare test data
        InvoiceResponseDto invoice = new InvoiceResponseDto();
        invoice.setId(1L);
        invoice.setInvoiceNumber("INV-001");
        invoice.setCustomerName("Test Customer");
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setAmount(new BigDecimal("100.00"));
        invoice.setStatus(Invoice.InvoiceStatus.PENDING);

        // Mock service method
        when(invoiceService.getInvoiceByInvoiceNumber("INV-001")).thenReturn(invoice);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/invoices/number/INV-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.invoiceNumber").value("INV-001"))
                .andExpect(jsonPath("$.customerName").value("Test Customer"));
    }

    @Test
    public void testGetInvoiceByInvoiceNumber_Error() throws Exception {
        // Mock service method to throw exception
        when(invoiceService.getInvoiceByInvoiceNumber("INV-999")).thenThrow(
                new EntityNotFoundException("Invoice with number INV-999 not found"));

        // Perform GET request and validate response
        mockMvc.perform(get("/api/invoices/number/INV-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Invoice with number INV-999 not found"));
    }

    @Test
    public void testCreateInvoice_Success() throws Exception {
        // Prepare request data
        InvoiceRequestDto requestDto = new InvoiceRequestDto();
        requestDto.setInvoiceNumber("INV-003");
        requestDto.setCustomerName("New Customer");
        requestDto.setInvoiceDate(LocalDate.now());
        requestDto.setDescription("Test Invoice");

        LineItemRequestDto lineItem = new LineItemRequestDto();
        lineItem.setDescription("Test Item");
        lineItem.setPrice(new BigDecimal("50.00"));
        lineItem.setQuantity(2);
        requestDto.getLineItems().add(lineItem);

        // Prepare response data
        InvoiceResponseDto responseDto = new InvoiceResponseDto();
        responseDto.setId(3L);
        responseDto.setInvoiceNumber("INV-003");
        responseDto.setCustomerName("New Customer");
        responseDto.setInvoiceDate(LocalDate.now());
        responseDto.setAmount(new BigDecimal("100.00"));
        responseDto.setStatus(Invoice.InvoiceStatus.PENDING);

        // Mock service method
        when(invoiceService.createInvoice(any(InvoiceRequestDto.class))).thenReturn(responseDto);

        // Perform POST request and validate response
        mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.invoiceNumber").value("INV-003"))
                .andExpect(jsonPath("$.customerName").value("New Customer"))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    public void testCreateInvoice_Error() throws Exception {
        // Prepare invalid request data (missing required fields)
        InvoiceRequestDto requestDto = new InvoiceRequestDto();
        // Missing invoiceNumber, customerName, and invoiceDate which are required

        // Perform POST request and validate response
        mockMvc.perform(post("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
                // The response will contain validation errors for the missing fields
    }

    @Test
    public void testUpdateInvoice_Success() throws Exception {
        // Prepare request data
        UpdateInvoiceRequestDto requestDto = new UpdateInvoiceRequestDto();
        requestDto.setInvoiceNumber("INV-001");

        LineItemRequestDto lineItem = new LineItemRequestDto();
        lineItem.setDescription("Updated Item");
        lineItem.setPrice(new BigDecimal("75.00"));
        lineItem.setQuantity(2);
        requestDto.getLineItems().add(lineItem);

        // Prepare response data
        InvoiceResponseDto responseDto = new InvoiceResponseDto();
        responseDto.setId(1L);
        responseDto.setInvoiceNumber("INV-001");
        responseDto.setCustomerName("Test Customer");
        responseDto.setInvoiceDate(LocalDate.now());
        responseDto.setAmount(new BigDecimal("150.00"));
        responseDto.setStatus(Invoice.InvoiceStatus.PENDING);

        // Mock service method
        when(invoiceService.updateInvoice(any(UpdateInvoiceRequestDto.class))).thenReturn(responseDto);

        // Perform PUT request and validate response
        mockMvc.perform(put("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.invoiceNumber").value("INV-001"))
                .andExpect(jsonPath("$.amount").value(150.00));
    }

    @Test
    public void testUpdateInvoice_Error() throws Exception {
        // Prepare request data for non-existent invoice
        UpdateInvoiceRequestDto requestDto = new UpdateInvoiceRequestDto();
        requestDto.setInvoiceNumber("INV-999"); // Non-existent invoice number

        LineItemRequestDto lineItem = new LineItemRequestDto();
        lineItem.setDescription("Updated Item");
        lineItem.setPrice(new BigDecimal("75.00"));
        lineItem.setQuantity(2);
        requestDto.getLineItems().add(lineItem);

        // Mock service method to throw exception
        when(invoiceService.updateInvoice(any(UpdateInvoiceRequestDto.class))).thenThrow(
                new EntityNotFoundException("Invoice with number INV-999 not found"));

        // Perform PUT request and validate response
        mockMvc.perform(put("/api/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Invoice with number INV-999 not found"));
    }

    @Test
    public void testPayInvoice_Success() throws Exception {
        // Prepare request data
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setInvoiceNumber("INV-001");
        requestDto.setPaymentDate(LocalDate.now());
        requestDto.setAmount(new BigDecimal("100.00"));
        requestDto.setPaymentMethod("Credit Card");

        // Prepare response data
        InvoiceResponseDto responseDto = new InvoiceResponseDto();
        responseDto.setId(1L);
        responseDto.setInvoiceNumber("INV-001");
        responseDto.setCustomerName("Test Customer");
        responseDto.setInvoiceDate(LocalDate.now());
        responseDto.setAmount(new BigDecimal("100.00"));
        responseDto.setStatus(Invoice.InvoiceStatus.PAID);

        // Mock service method
        when(invoiceService.payInvoice(any(PaymentRequestDto.class))).thenReturn(responseDto);

        // Perform POST request and validate response
        mockMvc.perform(post("/api/invoices/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.invoiceNumber").value("INV-001"))
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    public void testPayInvoice_Error() throws Exception {
        // Prepare request data for non-existent invoice
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setInvoiceNumber("INV-999"); // Non-existent invoice number
        requestDto.setPaymentDate(LocalDate.now());
        requestDto.setAmount(new BigDecimal("100.00"));
        requestDto.setPaymentMethod("Credit Card");

        // Mock service method to throw exception
        when(invoiceService.payInvoice(any(PaymentRequestDto.class))).thenThrow(
                new EntityNotFoundException("Invoice with number INV-999 not found"));

        // Perform POST request and validate response
        mockMvc.perform(post("/api/invoices/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Invoice with number INV-999 not found"));
    }
}
