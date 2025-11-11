package org.supplychain.supplychain.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
/*
we use testproperty if we use application-test.properties not yml
 */
//@TestPropertySource(locations = "classpath:application-test.yml")
//@SpringBootTest
@AutoConfigureMockMvc
public class SupplierIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllSuppliersIntegration() throws Exception {
        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk());
    }
}
