package com.example.invoicesbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.invoicesbackend.cqrs.command.invoice.CreateInvoiceCommand;
import com.example.invoicesbackend.cqrs.command.invoice.CreateInvoiceCommandHandler;
import com.example.invoicesbackend.cqrs.command.invoice.UpdateInvoiceCommand;
import com.example.invoicesbackend.cqrs.command.invoice.UpdateInvoiceCommandHandler;
import com.example.invoicesbackend.cqrs.query.invoice.GetAllInvoicesQuery;
import com.example.invoicesbackend.cqrs.query.invoice.GetAllInvoicesQueryHandler;
import com.example.invoicesbackend.cqrs.query.invoice.GetInvoiceByIdQuery;
import com.example.invoicesbackend.cqrs.query.invoice.GetInvoiceByIdQueryHandler;
import com.example.invoicesbackend.cqrs.query.invoice.GetInvoiceByInvoiceNumberQuery;
import com.example.invoicesbackend.cqrs.query.invoice.GetInvoiceByInvoiceNumberQueryHandler;
import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.request.UpdateInvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;

/**
 * Service for invoice operations using CQRS pattern.
 * This service delegates to command and query handlers.
 */
@Service
public class InvoiceService {

    private final CreateInvoiceCommandHandler createInvoiceCommandHandler;
    private final UpdateInvoiceCommandHandler updateInvoiceCommandHandler;
    private final GetAllInvoicesQueryHandler getAllInvoicesQueryHandler;
    private final GetInvoiceByIdQueryHandler getInvoiceByIdQueryHandler;
    private final GetInvoiceByInvoiceNumberQueryHandler getInvoiceByInvoiceNumberQueryHandler;

    @Autowired
    public InvoiceService(
            CreateInvoiceCommandHandler createInvoiceCommandHandler,
            UpdateInvoiceCommandHandler updateInvoiceCommandHandler,
            GetAllInvoicesQueryHandler getAllInvoicesQueryHandler,
            GetInvoiceByIdQueryHandler getInvoiceByIdQueryHandler,
            GetInvoiceByInvoiceNumberQueryHandler getInvoiceByInvoiceNumberQueryHandler) {
        this.createInvoiceCommandHandler = createInvoiceCommandHandler;
        this.updateInvoiceCommandHandler = updateInvoiceCommandHandler;
        this.getAllInvoicesQueryHandler = getAllInvoicesQueryHandler;
        this.getInvoiceByIdQueryHandler = getInvoiceByIdQueryHandler;
        this.getInvoiceByInvoiceNumberQueryHandler = getInvoiceByInvoiceNumberQueryHandler;
    }

    /**
     * Get all invoices.
     * 
     * @return List of invoice response DTOs
     */
    public List<InvoiceResponseDto> getAllInvoices() {
        return getAllInvoicesQueryHandler.handle(new GetAllInvoicesQuery());
    }

    /**
     * Get an invoice by its ID.
     * 
     * @param id The ID of the invoice to retrieve
     * @return The invoice response DTO
     */
    public InvoiceResponseDto getInvoiceById(Long id) {
        return getInvoiceByIdQueryHandler.handle(new GetInvoiceByIdQuery(id));
    }

    /**
     * Get an invoice by its invoice number.
     * 
     * @param invoiceNumber The invoice number of the invoice to retrieve
     * @return The invoice response DTO
     */
    public InvoiceResponseDto getInvoiceByInvoiceNumber(String invoiceNumber) {
        return getInvoiceByInvoiceNumberQueryHandler.handle(new GetInvoiceByInvoiceNumberQuery(invoiceNumber));
    }

    /**
     * Create a new invoice.
     * 
     * @param invoiceRequestDto The invoice request DTO
     * @return The created invoice response DTO
     */
    public InvoiceResponseDto createInvoice(InvoiceRequestDto invoiceRequestDto) {
        return createInvoiceCommandHandler.handle(new CreateInvoiceCommand(invoiceRequestDto));
    }

    /**
     * Update an existing invoice.
     * 
     * @param updateInvoiceRequestDto The update invoice request DTO
     * @return The updated invoice response DTO
     */
    public InvoiceResponseDto updateInvoice(UpdateInvoiceRequestDto updateInvoiceRequestDto) {
        return updateInvoiceCommandHandler.handle(new UpdateInvoiceCommand(updateInvoiceRequestDto));
    }
}
