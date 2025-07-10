package com.example.invoicesbackend.mapper;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.model.Invoice;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-10T08:31:59-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Homebrew)"
)
@Component
public class InvoiceMapperImpl implements InvoiceMapper {

    @Autowired
    private LineItemMapper lineItemMapper;

    @Override
    public Invoice toEntity(InvoiceRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber( requestDto.getInvoiceNumber() );
        invoice.setCustomerName( requestDto.getCustomerName() );
        invoice.setInvoiceDate( requestDto.getInvoiceDate() );
        invoice.setDueDate( requestDto.getDueDate() );
        invoice.setDescription( requestDto.getDescription() );
        invoice.setStatus( requestDto.getStatus() );

        return invoice;
    }

    @Override
    public InvoiceResponseDto toDto(Invoice invoice) {
        if ( invoice == null ) {
            return null;
        }

        InvoiceResponseDto invoiceResponseDto = new InvoiceResponseDto();

        invoiceResponseDto.setId( invoice.getId() );
        invoiceResponseDto.setInvoiceNumber( invoice.getInvoiceNumber() );
        invoiceResponseDto.setCustomerName( invoice.getCustomerName() );
        invoiceResponseDto.setInvoiceDate( invoice.getInvoiceDate() );
        invoiceResponseDto.setDueDate( invoice.getDueDate() );
        invoiceResponseDto.setAmount( invoice.getAmount() );
        invoiceResponseDto.setDescription( invoice.getDescription() );
        invoiceResponseDto.setStatus( invoice.getStatus() );
        invoiceResponseDto.setLineItems( lineItemMapper.toDtoList( invoice.getLineItems() ) );

        return invoiceResponseDto;
    }

    @Override
    public List<InvoiceResponseDto> toDtoList(List<Invoice> invoices) {
        if ( invoices == null ) {
            return null;
        }

        List<InvoiceResponseDto> list = new ArrayList<InvoiceResponseDto>( invoices.size() );
        for ( Invoice invoice : invoices ) {
            list.add( toDto( invoice ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(InvoiceRequestDto requestDto, Invoice invoice) {
        if ( requestDto == null ) {
            return;
        }

        if ( requestDto.getInvoiceNumber() != null ) {
            invoice.setInvoiceNumber( requestDto.getInvoiceNumber() );
        }
        if ( requestDto.getCustomerName() != null ) {
            invoice.setCustomerName( requestDto.getCustomerName() );
        }
        if ( requestDto.getInvoiceDate() != null ) {
            invoice.setInvoiceDate( requestDto.getInvoiceDate() );
        }
        if ( requestDto.getDueDate() != null ) {
            invoice.setDueDate( requestDto.getDueDate() );
        }
        if ( requestDto.getDescription() != null ) {
            invoice.setDescription( requestDto.getDescription() );
        }
        if ( requestDto.getStatus() != null ) {
            invoice.setStatus( requestDto.getStatus() );
        }
    }
}
