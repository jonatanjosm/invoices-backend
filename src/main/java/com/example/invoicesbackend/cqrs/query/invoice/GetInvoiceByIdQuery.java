package com.example.invoicesbackend.cqrs.query.invoice;

import com.example.invoicesbackend.cqrs.Query;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query to get an invoice by its ID.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInvoiceByIdQuery implements Query<InvoiceResponseDto> {
    private Long id;
}