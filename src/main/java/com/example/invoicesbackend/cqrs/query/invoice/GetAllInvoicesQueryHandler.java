package com.example.invoicesbackend.cqrs.query.invoice;

import com.example.invoicesbackend.cqrs.QueryHandler;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.mapper.InvoiceMapper;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handler for the GetAllInvoicesQuery.
 */
@Component
public class GetAllInvoicesQueryHandler implements QueryHandler<GetAllInvoicesQuery, List<InvoiceResponseDto>> {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Autowired
    public GetAllInvoicesQueryHandler(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public List<InvoiceResponseDto> handle(GetAllInvoicesQuery query) {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoiceMapper.toDtoList(invoices);
    }
}