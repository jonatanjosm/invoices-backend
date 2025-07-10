package com.example.invoicesbackend.mapper;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {

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
    }
}