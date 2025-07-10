package com.example.invoicesbackend.service;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.mapper.InvoiceMapper;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    public List<InvoiceResponseDto> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoiceMapper.toDtoList(invoices);
    }

    public InvoiceResponseDto getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));
        return invoiceMapper.toDto(invoice);
    }

    public InvoiceResponseDto getInvoiceByInvoiceNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with invoice number: " + invoiceNumber));
        return invoiceMapper.toDto(invoice);
    }

    public InvoiceResponseDto createInvoice(InvoiceRequestDto invoiceRequestDto) {
        if (invoiceRepository.existsByInvoiceNumber(invoiceRequestDto.getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + invoiceRequestDto.getInvoiceNumber() + " already exists");
        }
        Invoice invoice = invoiceMapper.toEntity(invoiceRequestDto);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(savedInvoice);
    }

    public InvoiceResponseDto updateInvoice(Long id, InvoiceRequestDto invoiceRequestDto) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        // Only update if the invoice number is not changed or the new invoice number doesn't exist
        if (!invoice.getInvoiceNumber().equals(invoiceRequestDto.getInvoiceNumber()) && 
            invoiceRepository.existsByInvoiceNumber(invoiceRequestDto.getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice with number " + invoiceRequestDto.getInvoiceNumber() + " already exists");
        }

        invoiceMapper.updateEntityFromDto(invoiceRequestDto, invoice);
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(updatedInvoice);
    }
}
