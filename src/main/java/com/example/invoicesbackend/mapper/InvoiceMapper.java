package com.example.invoicesbackend.mapper;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.model.Invoice;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LineItemMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lineItems", ignore = true)
    @Mapping(target = "amount", ignore = true)
    Invoice toEntity(InvoiceRequestDto requestDto);

    InvoiceResponseDto toDto(Invoice invoice);

    List<InvoiceResponseDto> toDtoList(List<Invoice> invoices);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lineItems", ignore = true)
    @Mapping(target = "amount", ignore = true)
    void updateEntityFromDto(InvoiceRequestDto requestDto, @MappingTarget Invoice invoice);

    @AfterMapping
    default void afterUpdateEntityFromDto(InvoiceRequestDto requestDto, @MappingTarget Invoice invoice, @Context LineItemMapper lineItemMapper) {
        // Update line items - only add new ones, don't clear existing ones
        if (requestDto.getLineItems() != null && !requestDto.getLineItems().isEmpty()) {
            // Add new line items
            requestDto.getLineItems().forEach(lineItemDto -> {
                // Use the injected LineItemMapper
                invoice.addLineItem(lineItemMapper.toEntity(lineItemDto));
            });

            // Recalculate the invoice amount
            invoice.calculateAmount();
        }
    }
}
