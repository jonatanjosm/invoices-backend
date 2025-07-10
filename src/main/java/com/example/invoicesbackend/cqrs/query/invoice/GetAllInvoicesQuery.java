package com.example.invoicesbackend.cqrs.query.invoice;

import com.example.invoicesbackend.cqrs.Query;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Query to get all invoices.
 */
@Data
@NoArgsConstructor
public class GetAllInvoicesQuery implements Query<List<InvoiceResponseDto>> {
    // No parameters needed for this query
}
