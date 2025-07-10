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
 * Handler for the GetInvoiceByInvoiceNumberQuery.
 */
@Component
public class GetInvoiceByInvoiceNumberQueryHandler implements QueryHandler<GetInvoiceByInvoiceNumberQuery, InvoiceResponseDto> {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Autowired
    public GetInvoiceByInvoiceNumberQueryHandler(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public InvoiceResponseDto handle(GetInvoiceByInvoiceNumberQuery query) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(query.getInvoiceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with invoice number: " + query.getInvoiceNumber()));
        return invoiceMapper.toDto(invoice);
    }
}