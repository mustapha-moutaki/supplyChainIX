package org.supplychain.supplychain.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllOrders() throws Exception {

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());
    }
}
