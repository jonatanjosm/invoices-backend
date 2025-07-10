package com.example.invoicesbackend.dto.response;

import com.example.invoicesbackend.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {

    private Long id;
    private String invoiceNumber;
    private String customerName;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private BigDecimal amount;
    private String description;
    private Invoice.InvoiceStatus status;
}