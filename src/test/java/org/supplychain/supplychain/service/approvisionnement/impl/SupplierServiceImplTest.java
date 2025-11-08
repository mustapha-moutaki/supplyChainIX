package org.supplychain.supplychain.service.approvisionnement.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.supplychain.supplychain.dto.supplier.SupplierDTO;
import org.supplychain.supplychain.mapper.modelSupplier.SupplierMapper;
import org.supplychain.supplychain.model.Supplier;
import org.supplychain.supplychain.repository.approvisionnement.SupplierRepository;
import org.supplychain.supplychain.service.modelSupplier.impl.SupplierServiceImpl;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierMapper supplierMapper;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    void testGetSupplierById() {

        Supplier supplier = new Supplier();
        supplier.setIdSupplier(1L);


        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        when(supplierMapper.toDTO(any())).thenReturn(new SupplierDTO());

        SupplierDTO dto = supplierService.getSupplierById(1L);


        assertNotNull(dto);
    }

    @Test
    void testCreateSupplier() {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setName("Test Supplier");

        Supplier supplierEntity = new Supplier();
        supplierEntity.setIdSupplier(1L);

        when(supplierMapper.toEntity(any(SupplierDTO.class))).thenReturn(supplierEntity);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplierEntity);
        when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(supplierDTO);

        SupplierDTO created = supplierService.createSupplier(supplierDTO);

        assertNotNull(created);
        assert(created.getName().equals("Test Supplier"));
    }
}
