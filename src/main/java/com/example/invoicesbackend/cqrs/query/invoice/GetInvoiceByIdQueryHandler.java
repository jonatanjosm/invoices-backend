package com.example.invoicesbackend.cqrs.query.invoice;

import com.example.invoicesbackend.cqrs.QueryHandler;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.mapper.InvoiceMapper;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

/**
 * Handler for the GetInvoiceByIdQuery.
 */
@Component
public class GetInvoiceByIdQueryHandler implements QueryHandler<GetInvoiceByIdQuery, InvoiceResponseDto> {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Autowired
    public GetInvoiceByIdQueryHandler(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public InvoiceResponseDto handle(GetInvoiceByIdQuery query) {
        Invoice invoice = invoiceRepository.findById(query.getId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + query.getId()));
        return invoiceMapper.toDto(invoice);
    }
}