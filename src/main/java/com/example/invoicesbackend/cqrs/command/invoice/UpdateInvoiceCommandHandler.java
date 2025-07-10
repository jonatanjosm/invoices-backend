package com.example.invoicesbackend.cqrs.command.invoice;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.invoicesbackend.cqrs.CommandHandler;
import com.example.invoicesbackend.dto.request.LineItemRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.mapper.InvoiceMapper;
import com.example.invoicesbackend.mapper.LineItemMapper;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.model.LineItem;
import com.example.invoicesbackend.repository.InvoiceRepository;
import com.example.invoicesbackend.repository.LineItemRepository;

/**
 * Handler for the UpdateInvoiceCommand.
 */
@Component
public class UpdateInvoiceCommandHandler implements CommandHandler<UpdateInvoiceCommand, InvoiceResponseDto> {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    private final LineItemRepository lineItemRepository;

    @Autowired
    public UpdateInvoiceCommandHandler(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, LineItemRepository lineItemRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.lineItemRepository = lineItemRepository;
    }

    @Override
    public InvoiceResponseDto handle(UpdateInvoiceCommand command) {
        Invoice invoice = invoiceRepository
              .findByInvoiceNumber(command.getUpdateInvoiceRequestDto().getInvoiceNumber())
              .orElseThrow(
                    () -> new EntityNotFoundException("Invoice not found with number: " + command.getUpdateInvoiceRequestDto().getInvoiceNumber()));

        // Only update if the invoice number is not changed or the new invoice number doesn't exist
        if (!invoice.getInvoiceNumber().equals(command.getUpdateInvoiceRequestDto().getInvoiceNumber()) && invoiceRepository.existsByInvoiceNumber(
              command.getUpdateInvoiceRequestDto().getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + command.getUpdateInvoiceRequestDto().getInvoiceNumber() + " already exists");
        }

        if (Invoice.InvoiceStatus.PAID.equals(invoice.getStatus())) {
            throw new IllegalArgumentException(
                  "Invoice with number " + command.getUpdateInvoiceRequestDto().getInvoiceNumber() + " has been already paid");
        }

        // Add the new line items to the invoice
        addItemsToInvoice(command.getUpdateInvoiceRequestDto().getLineItems(), invoice);

        // Calculate the total amount
        invoice.calculateAmount();

        // Save the invoice
        Invoice updatedInvoice = invoiceRepository.save(invoice);

        // Create a fresh DTO from the updated invoice to avoid duplication
        InvoiceResponseDto responseDto = invoiceMapper.toDto(updatedInvoice);
        return responseDto;
    }

    private BigDecimal calculateTotalAmount(List<LineItemRequestDto> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (LineItemRequestDto item : items) {
            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return totalAmount;
    }

    private void addItemsToInvoice(List<LineItemRequestDto> items, Invoice invoice) {
        // Create LineItem entities without saving them
        List<LineItem> lineItems = items.stream().map(lineItemDto -> {
            LineItem lineItem = LineItemMapper.INSTANCE.toEntity(lineItemDto);
            lineItem.calculateTotalAmount(); // Ensure totalAmount is calculated
            invoice.addLineItem(lineItem); // This sets up the bidirectional relationship
            return lineItem;
        }).collect(Collectors.toList());

        // Save all entities in a single operation
        if (!lineItems.isEmpty()) {
            lineItemRepository.saveAll(lineItems);
        }
    }
}
