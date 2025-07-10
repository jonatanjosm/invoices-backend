package com.example.invoicesbackend.cqrs.command.invoice;

import com.example.invoicesbackend.cqrs.CommandHandler;
import com.example.invoicesbackend.dto.request.LineItemRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.mapper.InvoiceMapper;
import com.example.invoicesbackend.mapper.LineItemMapper;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.model.LineItem;
import com.example.invoicesbackend.repository.InvoiceRepository;
import com.example.invoicesbackend.repository.LineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with number: " + command.getUpdateInvoiceRequestDto().getInvoiceNumber()));

        // Only update if the invoice number is not changed or the new invoice number doesn't exist
        if (!invoice.getInvoiceNumber().equals(command.getUpdateInvoiceRequestDto().getInvoiceNumber()) && invoiceRepository.existsByInvoiceNumber(
                command.getUpdateInvoiceRequestDto().getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + command.getUpdateInvoiceRequestDto().getInvoiceNumber() + " already exists");
        }

        List<LineItem> items = invoice.getLineItems();
        List<LineItem> savedItems = saveItems(command.getUpdateInvoiceRequestDto().getLineItems(), invoice);
        items.addAll(savedItems);

        invoice.setLineItems(items);

        invoice.setAmount(calculateTotalAmount(items.stream().map(LineItemMapper.INSTANCE::toDto).collect(Collectors.toList())));

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(updatedInvoice);
    }

    private BigDecimal calculateTotalAmount(List<LineItemRequestDto> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (LineItemRequestDto item : items) {
            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return totalAmount;
    }

    private List<LineItem> saveItems(List<LineItemRequestDto> items, Invoice invoice) {
        return items.stream().map(lineItemDto -> {
            LineItem lineItem = LineItemMapper.INSTANCE.toEntity(lineItemDto);
            lineItem.setInvoice(invoice);
            return lineItemRepository.save(lineItem);
        }).collect(Collectors.toList());
    }
}