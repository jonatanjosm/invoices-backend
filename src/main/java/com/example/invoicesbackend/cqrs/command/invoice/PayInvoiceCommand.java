package com.example.invoicesbackend.cqrs.command.invoice;

import com.example.invoicesbackend.cqrs.Command;
import com.example.invoicesbackend.dto.request.PaymentRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to pay an invoice.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayInvoiceCommand implements Command<InvoiceResponseDto> {
    private PaymentRequestDto paymentRequestDto;
}