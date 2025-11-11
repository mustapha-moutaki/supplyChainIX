package org.supplychain.supplychain.controller.approvisionnement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.supplychain.supplychain.dto.supplier.SupplierDTO;
import org.supplychain.supplychain.service.modelSupplier.SupplierService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplierController.class)
@ActiveProfiles("test")
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc; // Used to simulate HTTP requests

    @MockBean
    private SupplierService supplierService; // Mock service for controller

    private SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        // Create a fake supplier object to use in all tests
        supplierDTO = new SupplierDTO();
        supplierDTO.setName("testname");
        supplierDTO.setContact("John Doe");
        supplierDTO.setEmail("first@12.com");
        supplierDTO.setPhone("0654123456");
        supplierDTO.setRating(4.7);
        supplierDTO.setLeadTime(22);
        supplierDTO.setMaterialIds(List.of(1L));
    }

    @Test
    void testGetAllSuppliers() throws Exception {
        // Mock service response with one fake supplier
        Page<SupplierDTO> pageSuppliers = new PageImpl<>(List.of(supplierDTO));
        when(supplierService.getAllSuppliers(anyInt(), anyInt())).thenReturn(pageSuppliers);


        // Send GET request to /api/suppliers?page=0&size=10
        mockMvc.perform(get("/api/suppliers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.content[0].name").value("testname")); // Check name in response
    }

    @Test
    void testGetSupplierById() throws Exception {
        // Mock the response of service when called with id = 1
        when(supplierService.getSupplierById(1L)).thenReturn(supplierDTO);

        // Send GET request to /api/suppliers/1
        mockMvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.name").value("testname")); // Check name matches fake data
    }

    @Test
    void testCreateSupplier() throws Exception {
        // Mock service createSupplier to return the same fake supplier
        when(supplierService.createSupplier(any(SupplierDTO.class))).thenReturn(supplierDTO);

        // Send POST request with JSON body
        mockMvc.perform(post("/api/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(supplierDTO)))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.name").value("testname")) // Check name
                .andExpect(jsonPath("$.email").value("first@12.com")); // Check email
    }
}
