package com.example.invoicesbackend.mapper;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.request.LineItemRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.model.LineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {

    private final LineItemMapper lineItemMapper;

    @Autowired
    public InvoiceMapper(LineItemMapper lineItemMapper) {
        this.lineItemMapper = lineItemMapper;
    }

    public Invoice toEntity(InvoiceRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(requestDto.getInvoiceNumber());
        invoice.setCustomerName(requestDto.getCustomerName());
        invoice.setInvoiceDate(requestDto.getInvoiceDate());
        invoice.setDueDate(requestDto.getDueDate());
        invoice.setAmount(requestDto.getAmount());
        invoice.setDescription(requestDto.getDescription());
        invoice.setStatus(requestDto.getStatus());

        // Map line items
        if (requestDto.getLineItems() != null && !requestDto.getLineItems().isEmpty()) {
            for (LineItemRequestDto lineItemDto : requestDto.getLineItems()) {
                LineItem lineItem = lineItemMapper.toEntity(lineItemDto);
                invoice.addLineItem(lineItem);
            }
        }

        return invoice;
    }

    public InvoiceResponseDto toDto(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        InvoiceResponseDto responseDto = new InvoiceResponseDto();
        responseDto.setId(invoice.getId());
        responseDto.setInvoiceNumber(invoice.getInvoiceNumber());
        responseDto.setCustomerName(invoice.getCustomerName());
        responseDto.setInvoiceDate(invoice.getInvoiceDate());
        responseDto.setDueDate(invoice.getDueDate());
        responseDto.setAmount(invoice.getAmount());
        responseDto.setDescription(invoice.getDescription());
        responseDto.setStatus(invoice.getStatus());

        // Map line items
        if (invoice.getLineItems() != null && !invoice.getLineItems().isEmpty()) {
            responseDto.setLineItems(lineItemMapper.toDtoList(invoice.getLineItems()));
        }

        return responseDto;
    }

    public List<InvoiceResponseDto> toDtoList(List<Invoice> invoices) {
        return invoices.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDto(InvoiceRequestDto requestDto, Invoice invoice) {
        if (requestDto == null) {
            return;
        }

        invoice.setInvoiceNumber(requestDto.getInvoiceNumber());
        invoice.setCustomerName(requestDto.getCustomerName());
        invoice.setInvoiceDate(requestDto.getInvoiceDate());
        invoice.setDueDate(requestDto.getDueDate());
        invoice.setAmount(requestDto.getAmount());
        invoice.setDescription(requestDto.getDescription());
        invoice.setStatus(requestDto.getStatus());

        // Update line items
        if (requestDto.getLineItems() != null) {
            // Clear existing line items
            invoice.getLineItems().clear();

            // Add new line items
            for (LineItemRequestDto lineItemDto : requestDto.getLineItems()) {
                LineItem lineItem = lineItemMapper.toEntity(lineItemDto);
                invoice.addLineItem(lineItem);
            }
        }
    }
}
