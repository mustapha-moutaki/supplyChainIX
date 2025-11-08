package org.supplychain.supplychain.service.approvisionnement.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.supplychain.supplychain.dto.supplier.SupplierDTO;
import org.supplychain.supplychain.model.Supplier;
import org.supplychain.supplychain.repository.approvisionnement.SupplierRepository;
import org.supplychain.supplychain.service.modelSupplier.impl.SupplierServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier supplier;
    private SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        supplier = new Supplier();
        supplier.setIdSupplier(1L);
        supplier.setName("Atlas Supply");
        supplier.setContact("atlas@supply.com");

        supplierDTO = new SupplierDTO();
        supplierDTO.setId(1L);
        supplierDTO.setName("Atlas Supply");
        supplierDTO.setContact("atlas@supply.com");
    }

    @Test
    void testGetAllSuppliers() {
        // create a page with your supplier
        Page<Supplier> supplierPage = new PageImpl<>(List.of(supplier));

        // mock repository to return the page
        when(supplierRepository.findAll(PageRequest.of(0, 10))).thenReturn(supplierPage);

        // call service method
        Page<SupplierDTO> result = supplierService.getAllSuppliers(0, 10);

        // assertions
        assertEquals(1, result.getContent().size());
        assertEquals("Atlas Supply", result.getContent().get(0).getName());
    }



    @Test
    void testGetSupplierById() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        SupplierDTO result = supplierService.getSupplierById(1L);
        assertNotNull(result);
        assertEquals("Atlas Supply", result.getName());
    }

    @Test
    void testCreateSupplier() {
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SupplierDTO result = supplierService.createSupplier(supplierDTO);
        assertNotNull(result);
        assertEquals("Atlas Supply", result.getName());
    }
}
