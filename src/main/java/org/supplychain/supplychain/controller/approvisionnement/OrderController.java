package org.supplychain.supplychain.controller.approvisionnement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 1. Added for logging
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supplychain.supplychain.constants.supplierConstants.ApiConstants;
import org.supplychain.supplychain.dto.order.OrderDTO;
import org.supplychain.supplychain.service.modelSupplier.OrderServiec;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API + ApiConstants.ORDER_ENDPOINT)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders API", description = "APIs for managing orders")
public class OrderController {

    private final OrderServiec orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto) {
        log.info("REST request to create a new Order for customer: {}", dto.getCustomerId());
        OrderDTO result = orderService.createOrder(dto);
        log.info("Order created successfully with ID: {}");
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "update order")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO dto) {
        log.info("REST request to update Order ID: {} with data: {}", id, dto);
        OrderDTO result = orderService.updateOrder(id, dto);
        log.info("Order ID: {} updated successfully", id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "delete order")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.warn("REST request to DELETE Order ID: {}", id);
        orderService.deleteOrder(id);
        log.info("Order ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "get all orders")
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(Pageable pageable) {
        log.info("REST request to get a page of Orders: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<OrderDTO> result = orderService.getAllOrders(pageable);
        log.debug("Found {} total orders in database", result.getTotalElements());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "get order by id")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        log.info("REST request to get Order details for ID: {}", id);
        OrderDTO result = orderService.getOrderById(id);
        return ResponseEntity.ok(result);
    }
}