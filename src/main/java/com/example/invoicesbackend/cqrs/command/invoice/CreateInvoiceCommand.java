package com.example.invoicesbackend.cqrs.command.invoice;

import com.example.invoicesbackend.cqrs.Command;
import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to create a new invoice.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceCommand implements Command<InvoiceResponseDto> {
    private InvoiceRequestDto invoiceRequestDto;
}