package com.example.invoicesbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoDto {
    private Long id;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
}