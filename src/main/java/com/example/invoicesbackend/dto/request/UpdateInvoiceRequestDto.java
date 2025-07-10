package com.example.invoicesbackend.dto.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvoiceRequestDto {

    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    @Valid
    private List<LineItemRequestDto> lineItems = new ArrayList<>();
}
