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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

        if (optionalInvoice.isEmpty()) {
            throw new IllegalArgumentException("Invoice with number " + invoiceNumber + " not found");
        }

        Invoice invoice = optionalInvoice.get();

        // Validate invoice status
        if (Invoice.InvoiceStatus.PAID.equals(invoice.getStatus())) {
            throw new IllegalStateException("Invoice with number " + invoiceNumber + " is already PAID");
        }

        // Validate payment amount
        BigDecimal paymentAmount = command.getPaymentRequestDto().getAmount();
        if (paymentAmount.compareTo(invoice.getDebtAmount()) > 0) {
            throw new IllegalArgumentException("Payment amount " + paymentAmount +
                " it's bigger than invoice amount " + invoice.getAmount());
        }

        BigDecimal newDebtAmount = invoice.getDebtAmount().subtract(paymentAmount);
        invoice.setDebtAmount(newDebtAmount);

        if(paymentAmount.compareTo(invoice.getDebtAmount()) == 0){
            invoice.setDebtAmount(BigDecimal.ZERO);
            invoice.setStatus(Invoice.InvoiceStatus.PAID);
        } else {
            invoice.setStatus(Invoice.InvoiceStatus.PARTIALLY_PAID);
        }

        List<Payment> paymentList = Objects.nonNull(invoice.getPayment()) ?  invoice.getPayment() : new ArrayList<>();

        // Create and save payment
        Payment payment = new Payment();
        payment.setInvoiceId(invoice.getId());
        payment.setPaymentDate(command.getPaymentRequestDto().getPaymentDate());
        payment.setAmount(paymentAmount);
        payment.setPaymentMethod(command.getPaymentRequestDto().getPaymentMethod());

        paymentList.add(payment);
        // Update invoice status
        invoice.setPayment(paymentList);

        // Save changes
        Invoice savedInvoice = invoiceRepository.save(invoice);

        return invoiceMapper.toDto(savedInvoice);
    }
}