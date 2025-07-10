package com.example.invoicesbackend.repository;

import com.example.invoicesbackend.model.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Long> {
    List<LineItem> findByInvoiceId(Long invoiceId);
}