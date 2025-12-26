package org.supplychain.supplychain.controller.approvisionnement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 1. Added
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderDTO;
import org.supplychain.supplychain.service.modelSupplier.SupplierOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-orders")
@RequiredArgsConstructor
@Slf4j
public class SupplierOrderController {

    private final SupplierOrderService service;

    // Create a new order
    @PostMapping
    public ResponseEntity<SupplyOrderDTO> createOrder(
            @Valid @RequestBody SupplyOrderDTO dto) {
        log.info("REST request to create a supply order for supplier ID: {}", dto.getSupplierId());
        SupplyOrderDTO response = service.createOrder(dto);
        log.info("Supply order created successfully with date: {} and Order Number: {}",
                response.getOrderDate(), response.getOrderNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<SupplyOrderDTO> getOrder(@PathVariable Long id) {
        log.info("REST request to get supply order details for ID: {}", id);
        return ResponseEntity.ok(service.getOrderById(id));
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<SupplyOrderDTO>> getAllOrders() {
        log.info("REST request to get all supply orders");
        List<SupplyOrderDTO> orders = service.getAllOrders();
        log.debug("Total supply orders retrieved: {}", orders.size());
        return ResponseEntity.ok(orders);
    }

    // Update order
    @PutMapping("/{id}")
    public ResponseEntity<SupplyOrderDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody SupplyOrderDTO dto) {
        log.info("REST request to update supply order ID: {}", id);
        SupplyOrderDTO updated = service.updateOrder(id, dto);
        log.info("Supply order ID: {} updated successfully", id);
        return ResponseEntity.ok(updated);
    }

    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.warn("REST request to DELETE supply order ID: {}", id);
        service.deleteOrder(id);
        log.info("Supply order ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}