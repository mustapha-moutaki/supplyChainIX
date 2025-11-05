package org.supplychain.supplychain.controller.approvisionnement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderDTO;
import org.supplychain.supplychain.service.modelSupplier.SupplierOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-orders")
@RequiredArgsConstructor
public class SupplierOrderController {

    private final SupplierOrderService service;

    // Create a new order
    @PostMapping
    public ResponseEntity<SupplyOrderDTO> createOrder(
            @Valid @RequestBody SupplyOrderDTO dto) {
        SupplyOrderDTO response = service.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<SupplyOrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<SupplyOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    // Update order
    @PutMapping("/{id}")
    public ResponseEntity<SupplyOrderDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody SupplyOrderDTO dto) {
        SupplyOrderDTO updated = service.updateOrder(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
