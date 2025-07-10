package com.example.invoicesbackend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.request.LineItemRequestDto;
import com.example.invoicesbackend.dto.request.UpdateInvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.mapper.InvoiceMapper;
import com.example.invoicesbackend.mapper.LineItemMapper;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.model.LineItem;
import com.example.invoicesbackend.repository.InvoiceRepository;
import com.example.invoicesbackend.repository.LineItemRepository;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    private final LineItemRepository lineItemRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, LineItemRepository lineItemRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.lineItemRepository = lineItemRepository;
    }

    public List<InvoiceResponseDto> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoiceMapper.toDtoList(invoices);
    }

    public InvoiceResponseDto getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));
        return invoiceMapper.toDto(invoice);
    }

    public InvoiceResponseDto getInvoiceByInvoiceNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository
              .findByInvoiceNumber(invoiceNumber)
              .orElseThrow(() -> new EntityNotFoundException("Invoice not found with invoice number: " + invoiceNumber));
        return invoiceMapper.toDto(invoice);
    }

    public InvoiceResponseDto createInvoice(InvoiceRequestDto invoiceRequestDto) {
        if (invoiceRepository.existsByInvoiceNumber(invoiceRequestDto.getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + invoiceRequestDto.getInvoiceNumber() + " already exists");
        }
        Invoice invoice = invoiceMapper.toEntity(invoiceRequestDto);
        invoice.setAmount(calculateTotalAmount(invoiceRequestDto.getLineItems()));
        Invoice savedInvoice = invoiceRepository.save(invoice);

        List<LineItem> savedItems = saveItems(invoiceRequestDto.getLineItems(), savedInvoice);

        savedInvoice.setLineItems(savedItems);
        return invoiceMapper.toDto(savedInvoice);
    }

    public InvoiceResponseDto updateInvoice(UpdateInvoiceRequestDto invoiceRequestDto) {
        Invoice invoice = invoiceRepository
              .findByInvoiceNumber(invoiceRequestDto.getInvoiceNumber())
              .orElseThrow(() -> new EntityNotFoundException("Invoice not found with number: " + invoiceRequestDto.getInvoiceNumber()));

        // Only update if the invoice number is not changed or the new invoice number doesn't exist
        if (!invoice.getInvoiceNumber().equals(invoiceRequestDto.getInvoiceNumber()) && invoiceRepository.existsByInvoiceNumber(
              invoiceRequestDto.getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + invoiceRequestDto.getInvoiceNumber() + " already exists");
        }

        List<LineItem> items = invoice.getLineItems();
        List<LineItem> savedItems = saveItems(invoiceRequestDto.getLineItems(), invoice);
        items.addAll(savedItems);

        invoice.setLineItems(items);

        invoice.setAmount(calculateTotalAmount(items.stream().map(LineItemMapper.INSTANCE::toDto).collect(Collectors.toList())));
        //invoice.setLineItems(items);

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(updatedInvoice);
    }

    private BigDecimal calculateTotalAmount(List<LineItemRequestDto> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (LineItemRequestDto item : items) {
            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return totalAmount;
    }

    private List<LineItem> saveItems(List<LineItemRequestDto> items, Invoice invoice) {

        return items.stream().map(lineItemDto -> {
            LineItem lineItem = LineItemMapper.INSTANCE.toEntity(lineItemDto);
            lineItem.setInvoice(invoice);
            return lineItemRepository.save(lineItem);

        }).collect(Collectors.toList());

    }
}
