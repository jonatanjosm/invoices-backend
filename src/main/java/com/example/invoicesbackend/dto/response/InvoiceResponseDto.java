package com.example.invoicesbackend.dto.response;

import com.example.invoicesbackend.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {

    private Long id;
    private String invoiceNumber;
    private String customerName;
    private LocalDate invoiceDate;
    private BigDecimal amount;
    private BigDecimal debtAmount;
    private String description;
    private Invoice.InvoiceStatus status;
    private List<LineItemResponseDto> lineItems = new ArrayList<>();
    private List<PaymentInfoDto> paymentInfo;
}
