package com.example.invoicesbackend.service;

import com.example.invoicesbackend.dto.request.InvoiceRequestDto;
import com.example.invoicesbackend.dto.response.InvoiceResponseDto;
import com.example.invoicesbackend.mapper.InvoiceMapper;
import com.example.invoicesbackend.model.Invoice;
import com.example.invoicesbackend.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock(lenient = true)
    private InvoiceRepository invoiceRepository;

    @Mock(lenient = true)
    private InvoiceMapper invoiceMapper;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice testInvoice1;
    private Invoice testInvoice2;
    private List<Invoice> allInvoices;

    private InvoiceResponseDto testInvoiceResponseDto1;
    private InvoiceResponseDto testInvoiceResponseDto2;
    private List<InvoiceResponseDto> allInvoiceResponseDtos;

    @BeforeEach
    void setUp() {
        testInvoice1 = new Invoice();
        testInvoice1.setId(1L);
        testInvoice1.setInvoiceNumber("INV-001");
        testInvoice1.setCustomerName("Test Customer 1");
        testInvoice1.setInvoiceDate(LocalDate.of(2023, 1, 1));
        testInvoice1.setDueDate(LocalDate.of(2023, 1, 31));
        testInvoice1.setAmount(new BigDecimal("1000.00"));
        testInvoice1.setDescription("Test Invoice 1");
        testInvoice1.setStatus(Invoice.InvoiceStatus.PENDING);

        testInvoice2 = new Invoice();
        testInvoice2.setId(2L);
        testInvoice2.setInvoiceNumber("INV-002");
        testInvoice2.setCustomerName("Test Customer 2");
        testInvoice2.setInvoiceDate(LocalDate.of(2023, 2, 1));
        testInvoice2.setDueDate(LocalDate.of(2023, 2, 28));
        testInvoice2.setAmount(new BigDecimal("2000.00"));
        testInvoice2.setDescription("Test Invoice 2");
        testInvoice2.setStatus(Invoice.InvoiceStatus.PAID);

        allInvoices = Arrays.asList(testInvoice1, testInvoice2);

        testInvoiceResponseDto1 = new InvoiceResponseDto();
        testInvoiceResponseDto1.setId(1L);
        testInvoiceResponseDto1.setInvoiceNumber("INV-001");
        testInvoiceResponseDto1.setCustomerName("Test Customer 1");
        testInvoiceResponseDto1.setInvoiceDate(LocalDate.of(2023, 1, 1));
        testInvoiceResponseDto1.setDueDate(LocalDate.of(2023, 1, 31));
        testInvoiceResponseDto1.setAmount(new BigDecimal("1000.00"));
        testInvoiceResponseDto1.setDescription("Test Invoice 1");
        testInvoiceResponseDto1.setStatus(Invoice.InvoiceStatus.PENDING);

        testInvoiceResponseDto2 = new InvoiceResponseDto();
        testInvoiceResponseDto2.setId(2L);
        testInvoiceResponseDto2.setInvoiceNumber("INV-002");
        testInvoiceResponseDto2.setCustomerName("Test Customer 2");
        testInvoiceResponseDto2.setInvoiceDate(LocalDate.of(2023, 2, 1));
        testInvoiceResponseDto2.setDueDate(LocalDate.of(2023, 2, 28));
        testInvoiceResponseDto2.setAmount(new BigDecimal("2000.00"));
        testInvoiceResponseDto2.setDescription("Test Invoice 2");
        testInvoiceResponseDto2.setStatus(Invoice.InvoiceStatus.PAID);

        allInvoiceResponseDtos = Arrays.asList(testInvoiceResponseDto1, testInvoiceResponseDto2);

        // Setup mapper mocks
        when(invoiceMapper.toDto(testInvoice1)).thenReturn(testInvoiceResponseDto1);
        when(invoiceMapper.toDto(testInvoice2)).thenReturn(testInvoiceResponseDto2);
        when(invoiceMapper.toDtoList(allInvoices)).thenReturn(allInvoiceResponseDtos);
    }

    @Test
    void getAllInvoices_ShouldReturnAllInvoices() {
        when(invoiceRepository.findAll()).thenReturn(allInvoices);

        List<InvoiceResponseDto> result = invoiceService.getAllInvoices();

        assertEquals(2, result.size());
        assertEquals("INV-001", result.get(0).getInvoiceNumber());
        assertEquals("INV-002", result.get(1).getInvoiceNumber());
        verify(invoiceRepository, times(1)).findAll();
        verify(invoiceMapper, times(1)).toDtoList(allInvoices);
    }

    @Test
    void getInvoiceById_WhenInvoiceExists_ShouldReturnInvoice() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice1));

        InvoiceResponseDto result = invoiceService.getInvoiceById(1L);

        assertEquals(1L, result.getId());
        assertEquals("INV-001", result.getInvoiceNumber());
        assertEquals("Test Customer 1", result.getCustomerName());
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceMapper, times(1)).toDto(testInvoice1);
    }

    @Test
    void getInvoiceById_WhenInvoiceDoesNotExist_ShouldThrowException() {
        when(invoiceRepository.findById(3L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            invoiceService.getInvoiceById(3L);
        });

        assertEquals("Invoice not found with id: 3", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(3L);
    }

    @Test
    void getInvoiceByInvoiceNumber_WhenInvoiceExists_ShouldReturnInvoice() {
        when(invoiceRepository.findByInvoiceNumber("INV-001")).thenReturn(Optional.of(testInvoice1));

        InvoiceResponseDto result = invoiceService.getInvoiceByInvoiceNumber("INV-001");

        assertEquals(1L, result.getId());
        assertEquals("INV-001", result.getInvoiceNumber());
        assertEquals("Test Customer 1", result.getCustomerName());
        verify(invoiceRepository, times(1)).findByInvoiceNumber("INV-001");
        verify(invoiceMapper, times(1)).toDto(testInvoice1);
    }

    @Test
    void getInvoiceByInvoiceNumber_WhenInvoiceDoesNotExist_ShouldThrowException() {
        when(invoiceRepository.findByInvoiceNumber("INV-999")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            invoiceService.getInvoiceByInvoiceNumber("INV-999");
        });

        assertEquals("Invoice not found with invoice number: INV-999", exception.getMessage());
        verify(invoiceRepository, times(1)).findByInvoiceNumber("INV-999");
    }

    @Test
    void createInvoice_WhenInvoiceNumberDoesNotExist_ShouldCreateInvoice() {
        InvoiceRequestDto newInvoiceRequestDto = new InvoiceRequestDto();
        newInvoiceRequestDto.setInvoiceNumber("INV-003");
        newInvoiceRequestDto.setCustomerName("New Customer");
        newInvoiceRequestDto.setInvoiceDate(LocalDate.of(2023, 3, 1));
        newInvoiceRequestDto.setDueDate(LocalDate.of(2023, 3, 31));
        newInvoiceRequestDto.setAmount(new BigDecimal("3000.00"));
        newInvoiceRequestDto.setDescription("New Invoice");
        newInvoiceRequestDto.setStatus(Invoice.InvoiceStatus.PENDING);

        Invoice newInvoice = new Invoice();
        newInvoice.setInvoiceNumber("INV-003");
        newInvoice.setCustomerName("New Customer");
        newInvoice.setInvoiceDate(LocalDate.of(2023, 3, 1));
        newInvoice.setDueDate(LocalDate.of(2023, 3, 31));
        newInvoice.setAmount(new BigDecimal("3000.00"));
        newInvoice.setDescription("New Invoice");
        newInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        Invoice savedInvoice = new Invoice();
        savedInvoice.setId(3L);
        savedInvoice.setInvoiceNumber("INV-003");
        savedInvoice.setCustomerName("New Customer");
        savedInvoice.setInvoiceDate(LocalDate.of(2023, 3, 1));
        savedInvoice.setDueDate(LocalDate.of(2023, 3, 31));
        savedInvoice.setAmount(new BigDecimal("3000.00"));
        savedInvoice.setDescription("New Invoice");
        savedInvoice.setStatus(Invoice.InvoiceStatus.PENDING);

        InvoiceResponseDto savedInvoiceResponseDto = new InvoiceResponseDto();
        savedInvoiceResponseDto.setId(3L);
        savedInvoiceResponseDto.setInvoiceNumber("INV-003");
        savedInvoiceResponseDto.setCustomerName("New Customer");
        savedInvoiceResponseDto.setInvoiceDate(LocalDate.of(2023, 3, 1));
        savedInvoiceResponseDto.setDueDate(LocalDate.of(2023, 3, 31));
        savedInvoiceResponseDto.setAmount(new BigDecimal("3000.00"));
        savedInvoiceResponseDto.setDescription("New Invoice");
        savedInvoiceResponseDto.setStatus(Invoice.InvoiceStatus.PENDING);

        when(invoiceRepository.existsByInvoiceNumber("INV-003")).thenReturn(false);
        when(invoiceMapper.toEntity(newInvoiceRequestDto)).thenReturn(newInvoice);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);
        when(invoiceMapper.toDto(savedInvoice)).thenReturn(savedInvoiceResponseDto);

        InvoiceResponseDto result = invoiceService.createInvoice(newInvoiceRequestDto);

        assertEquals(3L, result.getId());
        assertEquals("INV-003", result.getInvoiceNumber());
        assertEquals("New Customer", result.getCustomerName());
        verify(invoiceRepository, times(1)).existsByInvoiceNumber("INV-003");
        verify(invoiceMapper, times(1)).toEntity(newInvoiceRequestDto);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(invoiceMapper, times(1)).toDto(savedInvoice);
    }

    @Test
    void createInvoice_WhenInvoiceNumberExists_ShouldThrowException() {
        InvoiceRequestDto newInvoiceRequestDto = new InvoiceRequestDto();
        newInvoiceRequestDto.setInvoiceNumber("INV-001");
        newInvoiceRequestDto.setCustomerName("New Customer");
        newInvoiceRequestDto.setInvoiceDate(LocalDate.of(2023, 3, 1));
        newInvoiceRequestDto.setDueDate(LocalDate.of(2023, 3, 31));
        newInvoiceRequestDto.setAmount(new BigDecimal("3000.00"));
        newInvoiceRequestDto.setDescription("New Invoice");
        newInvoiceRequestDto.setStatus(Invoice.InvoiceStatus.PENDING);

        when(invoiceRepository.existsByInvoiceNumber("INV-001")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(newInvoiceRequestDto);
        });

        assertEquals("Invoice with number INV-001 already exists", exception.getMessage());
        verify(invoiceRepository, times(1)).existsByInvoiceNumber("INV-001");
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    void updateInvoice_WhenInvoiceExistsAndInvoiceNumberNotChanged_ShouldUpdateInvoice() {
        InvoiceRequestDto updatedInvoiceRequestDto = new InvoiceRequestDto();
        updatedInvoiceRequestDto.setInvoiceNumber("INV-001");
        updatedInvoiceRequestDto.setCustomerName("Updated Customer");
        updatedInvoiceRequestDto.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoiceRequestDto.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoiceRequestDto.setAmount(new BigDecimal("1500.00"));
        updatedInvoiceRequestDto.setDescription("Updated Invoice");
        updatedInvoiceRequestDto.setStatus(Invoice.InvoiceStatus.PAID);

        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setId(1L);
        updatedInvoice.setInvoiceNumber("INV-001");
        updatedInvoice.setCustomerName("Updated Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        InvoiceResponseDto updatedInvoiceResponseDto = new InvoiceResponseDto();
        updatedInvoiceResponseDto.setId(1L);
        updatedInvoiceResponseDto.setInvoiceNumber("INV-001");
        updatedInvoiceResponseDto.setCustomerName("Updated Customer");
        updatedInvoiceResponseDto.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoiceResponseDto.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoiceResponseDto.setAmount(new BigDecimal("1500.00"));
        updatedInvoiceResponseDto.setDescription("Updated Invoice");
        updatedInvoiceResponseDto.setStatus(Invoice.InvoiceStatus.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice1));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(updatedInvoice);
        when(invoiceMapper.toDto(updatedInvoice)).thenReturn(updatedInvoiceResponseDto);

        InvoiceResponseDto result = invoiceService.updateInvoice(1L, updatedInvoiceRequestDto);

        assertEquals("INV-001", result.getInvoiceNumber());
        assertEquals("Updated Customer", result.getCustomerName());
        assertEquals(new BigDecimal("1500.00"), result.getAmount());
        assertEquals(Invoice.InvoiceStatus.PAID, result.getStatus());
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(invoiceMapper, times(1)).toDto(updatedInvoice);
    }

    @Test
    void updateInvoice_WhenInvoiceExistsAndInvoiceNumberChangedToNonExisting_ShouldUpdateInvoice() {
        InvoiceRequestDto updatedInvoiceRequestDto = new InvoiceRequestDto();
        updatedInvoiceRequestDto.setInvoiceNumber("INV-003");
        updatedInvoiceRequestDto.setCustomerName("Updated Customer");
        updatedInvoiceRequestDto.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoiceRequestDto.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoiceRequestDto.setAmount(new BigDecimal("1500.00"));
        updatedInvoiceRequestDto.setDescription("Updated Invoice");
        updatedInvoiceRequestDto.setStatus(Invoice.InvoiceStatus.PAID);

        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setId(1L);
        updatedInvoice.setInvoiceNumber("INV-003");
        updatedInvoice.setCustomerName("Updated Customer");
        updatedInvoice.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoice.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoice.setAmount(new BigDecimal("1500.00"));
        updatedInvoice.setDescription("Updated Invoice");
        updatedInvoice.setStatus(Invoice.InvoiceStatus.PAID);

        InvoiceResponseDto updatedInvoiceResponseDto = new InvoiceResponseDto();
        updatedInvoiceResponseDto.setId(1L);
        updatedInvoiceResponseDto.setInvoiceNumber("INV-003");
        updatedInvoiceResponseDto.setCustomerName("Updated Customer");
        updatedInvoiceResponseDto.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoiceResponseDto.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoiceResponseDto.setAmount(new BigDecimal("1500.00"));
        updatedInvoiceResponseDto.setDescription("Updated Invoice");
        updatedInvoiceResponseDto.setStatus(Invoice.InvoiceStatus.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice1));
        when(invoiceRepository.existsByInvoiceNumber("INV-003")).thenReturn(false);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(updatedInvoice);
        when(invoiceMapper.toDto(updatedInvoice)).thenReturn(updatedInvoiceResponseDto);

        InvoiceResponseDto result = invoiceService.updateInvoice(1L, updatedInvoiceRequestDto);

        assertEquals("INV-003", result.getInvoiceNumber());
        assertEquals("Updated Customer", result.getCustomerName());
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).existsByInvoiceNumber("INV-003");
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(invoiceMapper, times(1)).toDto(updatedInvoice);
    }

    @Test
    void updateInvoice_WhenInvoiceExistsAndInvoiceNumberChangedToExisting_ShouldThrowException() {
        InvoiceRequestDto updatedInvoiceRequestDto = new InvoiceRequestDto();
        updatedInvoiceRequestDto.setInvoiceNumber("INV-002");
        updatedInvoiceRequestDto.setCustomerName("Updated Customer");
        updatedInvoiceRequestDto.setInvoiceDate(LocalDate.of(2023, 1, 1));
        updatedInvoiceRequestDto.setDueDate(LocalDate.of(2023, 1, 31));
        updatedInvoiceRequestDto.setAmount(new BigDecimal("1500.00"));
        updatedInvoiceRequestDto.setDescription("Updated Invoice");
        updatedInvoiceRequestDto.setStatus(Invoice.InvoiceStatus.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(testInvoice1));
        when(invoiceRepository.existsByInvoiceNumber("INV-002")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.updateInvoice(1L, updatedInvoiceRequestDto);
        });

        assertEquals("Invoice with number INV-002 already exists", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).existsByInvoiceNumber("INV-002");
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }
}
