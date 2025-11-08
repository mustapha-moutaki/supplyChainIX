package org.supplychain.supplychain.controller.approvisionnement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.supplychain.supplychain.controller.approvisionnement.SupplierController;
import org.supplychain.supplychain.dto.supplier.SupplierDTO;
import org.supplychain.supplychain.service.modelSupplier.SupplierService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SupplierControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(supplierController).build();

        supplierDTO = new SupplierDTO();
        supplierDTO.setId(1L);
        supplierDTO.setName("Atlas Supply");
        supplierDTO.setContact("atlas@supply.com");
    }

    @Test
    void testGetAllSuppliers() throws Exception {
        Page<SupplierDTO> pageSuppliers = new PageImpl<>(List.of(supplierDTO));

        when(supplierService.getAllSuppliers(0, 10)).thenReturn(pageSuppliers);

        mockMvc.perform(get("/api/suppliers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Atlas Supply"));
    }


    @Test
    void testGetSupplierById() throws Exception {
        when(supplierService.getSupplierById(1L)).thenReturn(supplierDTO);

        mockMvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atlas Supply"));
    }

    @Test
    void testCreateSupplier() throws Exception {
        when(supplierService.createSupplier(any(SupplierDTO.class))).thenReturn(supplierDTO);

        mockMvc.perform(post("/api/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(supplierDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atlas Supply"));
    }
}
