package com.example.invoicesbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemResponseDto {

    private Long id;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
}