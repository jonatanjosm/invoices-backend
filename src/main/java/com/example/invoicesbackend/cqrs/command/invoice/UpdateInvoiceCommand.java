package com.example.invoicesbackend.cqrs.command.invoice;

import com.example.invoicesbackend.cqrs.Command;
import com.example.invoicesbackend.dto.request.UpdateInvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to update an existing invoice.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvoiceCommand implements Command<InvoiceResponseDto> {
    private UpdateInvoiceRequestDto updateInvoiceRequestDto;
}