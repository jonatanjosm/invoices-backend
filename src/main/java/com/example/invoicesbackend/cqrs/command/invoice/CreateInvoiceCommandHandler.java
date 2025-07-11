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

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for the CreateInvoiceCommand.
 */
@Component
public class CreateInvoiceCommandHandler implements CommandHandler<CreateInvoiceCommand, InvoiceResponseDto> {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final LineItemRepository lineItemRepository;

    @Autowired
    public CreateInvoiceCommandHandler(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, LineItemRepository lineItemRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.lineItemRepository = lineItemRepository;
    }

    @Override
    public InvoiceResponseDto handle(CreateInvoiceCommand command) {
        if (invoiceRepository.existsByInvoiceNumber(command.getInvoiceRequestDto().getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + command.getInvoiceRequestDto().getInvoiceNumber() + " already exists");
        }
        
        Invoice invoice = invoiceMapper.toEntity(command.getInvoiceRequestDto());
        invoice.setAmount(calculateTotalAmount(command.getInvoiceRequestDto().getLineItems()));
        invoice.setDebtAmount(invoice.getAmount());
        Invoice savedInvoice = invoiceRepository.save(invoice);

        List<LineItem> savedItems = saveItems(command.getInvoiceRequestDto().getLineItems(), savedInvoice);

        savedInvoice.setLineItems(savedItems);
        return invoiceMapper.toDto(savedInvoice);
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