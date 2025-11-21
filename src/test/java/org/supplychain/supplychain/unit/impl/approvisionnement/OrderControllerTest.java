package org.supplychain.supplychain.unit.impl.approvisionnement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.supplychain.supplychain.controller.approvisionnement.OrderController;
import org.supplychain.supplychain.dto.order.OrderDTO;
import org.supplychain.supplychain.service.modelSupplier.OrderServiec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderServiec orderService;

    // ------------------- DELETE Order -------------------
    // here because we use spring security and we disable the csrf the best way in testing is using csrf
    @Test
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void testDeleteOrderWithUser() throws Exception {
        mockMvc.perform(delete("/api/orders/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService).deleteOrder(1L);
    }

    // ------------------- CREATE Order -------------------
    @Test
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void testCreateOrder() throws Exception {
        OrderDTO request = new OrderDTO();
        request.setId(1L);

        Mockito.when(orderService.createOrder(any(OrderDTO.class))).thenReturn(request);

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // ------------------- UPDATE Order -------------------
    @Test
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void testUpdateOrder() throws Exception {
        OrderDTO dto = new OrderDTO();
        dto.setId(1L);

        Mockito.when(orderService.updateOrder(eq(1L), any(OrderDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/orders/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // ------------------- GET Order by ID -------------------
    @Test
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void testGetOrderById() throws Exception {
        OrderDTO dto = new OrderDTO();
        dto.setId(10L);

        Mockito.when(orderService.getOrderById(10L)).thenReturn(dto);

        mockMvc.perform(get("/api/orders/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    // ------------------- GET All Orders -------------------
    @Test
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void testGetAllOrders() throws Exception {
        OrderDTO dto = new OrderDTO();
        dto.setId(1L);

        Page<OrderDTO> page = new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 10), 1);

        Mockito.when(orderService.getAllOrders(any())).thenReturn(page);

        mockMvc.perform(get("/api/orders?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }
}
