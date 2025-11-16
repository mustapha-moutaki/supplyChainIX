package org.supplychain.supplychain.unit.impl.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.supplychain.supplychain.dto.modelDelivery.CustomerDto;
import org.supplychain.supplychain.exception.DuplicateResourceException;
import org.supplychain.supplychain.exception.ResourceNotFoundException;
import org.supplychain.supplychain.mapper.modelDelivery.CustomerMapper;
import org.supplychain.supplychain.model.Customer;
import org.supplychain.supplychain.repository.modelDelivery.CustomerRepository;
import org.supplychain.supplychain.service.modelDelivery.impl.CustomerServiceImpl;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerMapper = mock(CustomerMapper.class);

        customerService = new CustomerServiceImpl(customerRepository, customerMapper);
    }


    @Test
    void testCreateCustomer_Success() {
        CustomerDto dto = new CustomerDto();
        dto.setEmail("test@test.com");

        Customer entity = new Customer();

        when(customerRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(customerMapper.toEntity(dto)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(entity);
        when(customerMapper.toDto(entity)).thenReturn(dto);

        CustomerDto result = customerService.createCustomer(dto);

        assertThat(result).isNotNull();
        verify(customerRepository).save(entity);
    }

    @Test
    void testCreateCustomer_EmailExists() {
        CustomerDto dto = new CustomerDto();
        dto.setEmail("duplicate@test.com");

        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testGetAllCustomers_NoFilter() {
        Customer customer = new Customer();
        CustomerDto dto = new CustomerDto();

        Page<Customer> page = new PageImpl<>(Collections.singletonList(customer));

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(customerMapper.toDto(customer)).thenReturn(dto);

        Page<CustomerDto> result = customerService.getAllCustomers(0, 10, null);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetAllCustomers_WithFilter() {
        Customer customer = new Customer();
        CustomerDto dto = new CustomerDto();

        Page<Customer> page = new PageImpl<>(Collections.singletonList(customer));

        when(customerRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                eq("ahmed"), eq("ahmed"), any(Pageable.class)
        )).thenReturn(page);

        when(customerMapper.toDto(customer)).thenReturn(dto);

        Page<CustomerDto> result = customerService.getAllCustomers(0, 10, "ahmed");

        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetAllCustomers_EmptyListThrowsException() {
        Page<Customer> emptyPage = new PageImpl<>(Collections.emptyList());

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () ->
                customerService.getAllCustomers(0, 10, null)
        );
    }


    @Test
    void testGetCustomerById_Success() {
        Customer customer = new Customer();
        CustomerDto dto = new CustomerDto();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(dto);

        CustomerDto result = customerService.getCustomerById(1L);

        assertNotNull(result);
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                customerService.getCustomerById(1L)
        );
    }


    @Test
    void testUpdateCustomer_Success() {
        Customer existing = new Customer();
        existing.setIdCustomer(1L);

        CustomerDto dto = new CustomerDto();
        dto.setName("New Name");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);
        when(customerMapper.toDto(existing)).thenReturn(dto);

        CustomerDto result = customerService.updateCustomer(1L, dto);

        assertEquals("New Name", result.getName());
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerDto dto = new CustomerDto();

        assertThrows(ResourceNotFoundException.class, () ->
                customerService.updateCustomer(1L, dto)
        );
    }

    @Test
    void testDeleteCustomer_Success() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository).delete(customer);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                customerService.deleteCustomer(1L)
        );
    }
}
