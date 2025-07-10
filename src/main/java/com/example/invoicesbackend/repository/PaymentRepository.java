package com.example.invoicesbackend.repository;

import com.example.invoicesbackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByInvoiceId(Long invoiceId);
}
