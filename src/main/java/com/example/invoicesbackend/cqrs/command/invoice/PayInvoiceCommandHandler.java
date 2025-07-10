package com.example.invoicesbackend.cqrs.command.invoice;

import com.example.invoicesbackend.cqrs.CommandHandler;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.mapper.InvoiceMapper;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.model.Payment;
import com.example.invoicesbackend.repository.InvoiceRepository;
import com.example.invoicesbackend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Handler for the PayInvoiceCommand.
 */
@Component
public class PayInvoiceCommandHandler implements CommandHandler<PayInvoiceCommand, InvoiceResponseDto> {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceMapper invoiceMapper;

    @Autowired
    public PayInvoiceCommandHandler(InvoiceRepository invoiceRepository, 
                                   PaymentRepository paymentRepository,
                                   InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    @Transactional
    public InvoiceResponseDto handle(PayInvoiceCommand command) {
        String invoiceNumber = command.getPaymentRequestDto().getInvoiceNumber();
        Optional<Invoice> optionalInvoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
        
        if (!optionalInvoice.isPresent()) {
            throw new IllegalArgumentException("Invoice with number " + invoiceNumber + " not found");
        }
        
        Invoice invoice = optionalInvoice.get();
        
        // Validate invoice status
        if (invoice.getStatus() != Invoice.InvoiceStatus.PENDING) {
            throw new IllegalStateException("Invoice with number " + invoiceNumber + " is not in PENDING status");
        }
        
        // Validate payment amount
        BigDecimal paymentAmount = command.getPaymentRequestDto().getAmount();
        if (paymentAmount.compareTo(invoice.getAmount()) != 0) {
            throw new IllegalArgumentException("Payment amount " + paymentAmount + 
                " does not match invoice amount " + invoice.getAmount());
        }
        
        // Create and save payment
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setPaymentDate(command.getPaymentRequestDto().getPaymentDate());
        payment.setAmount(paymentAmount);
        payment.setPaymentMethod(command.getPaymentRequestDto().getPaymentMethod());
        
        // Update invoice status
        invoice.setStatus(Invoice.InvoiceStatus.PAID);
        invoice.setPayment(payment);
        
        // Save changes
        Invoice savedInvoice = invoiceRepository.save(invoice);
        
        return invoiceMapper.toDto(savedInvoice);
    }
}