package com.example.invoicesbackend.mapper;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.dto.response.PaymentInfoDto;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.model.Payment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LineItemMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lineItems", ignore = true)
    @Mapping(target = "amount", ignore = true)
    Invoice toEntity(InvoiceRequestDto requestDto);

    @Mapping(target = "paymentInfo", expression = "java(mapPaymentInfo(invoice))")
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

    default PaymentInfoDto mapPaymentInfo(Invoice invoice) {
        // Only include payment info if the invoice has been paid
        if (invoice != null && Invoice.InvoiceStatus.PAID.equals(invoice.getStatus()) && invoice.getPayment() != null) {
            Payment payment = invoice.getPayment();
            PaymentInfoDto paymentInfoDto = new PaymentInfoDto();
            paymentInfoDto.setId(payment.getId());
            paymentInfoDto.setPaymentDate(payment.getPaymentDate());
            paymentInfoDto.setAmount(payment.getAmount());
            paymentInfoDto.setPaymentMethod(payment.getPaymentMethod());
            return paymentInfoDto;
        }
        return null;
    }
}
