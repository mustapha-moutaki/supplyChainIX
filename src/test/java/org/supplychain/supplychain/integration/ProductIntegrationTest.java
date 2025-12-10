package org.supplychain.supplychain.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.supplychain.supplychain.dto.BOM.BillOfMaterialDTO;
import org.supplychain.supplychain.dto.product.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateProductIntegration() throws Exception {
        String productJson = """
    {
        "name": "Product A",
        "unit": "kg",
        "cost": 50.0,
        "productionTime": 5,
        "billOfMaterials": [
            {
                "materialId": 1,
                "quantity": 10
            }
        ]
    }
    """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated());
    }



}
