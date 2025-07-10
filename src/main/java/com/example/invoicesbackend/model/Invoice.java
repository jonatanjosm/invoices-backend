package com.example.invoicesbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Invoice number is required")
    @Column(unique = true)
    private String invoiceNumber;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.PENDING;

    public enum InvoiceStatus {
        PENDING, PAID, OVERDUE
    }
}