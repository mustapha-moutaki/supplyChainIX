package org.supplychain.supplychain.controller.approvisionnement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supplychain.supplychain.constants.supplierConstants.ApiConstants;
import org.supplychain.supplychain.dto.order.OrderDTO;
import org.supplychain.supplychain.service.modelSupplier.OrderServiec;


import java.util.List;

@RestController
@RequestMapping(ApiConstants.API+ ApiConstants.ORDER_ENDPOINT)
@RequiredArgsConstructor
@Tag(name = "Orders API", description = "APIs for managing orders")
public class OrderController {


        private final OrderServiec orderService;

        @PostMapping
        @Operation(summary = "Create a new order")
        public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto) {
            return ResponseEntity.ok(orderService.createOrder(dto));
        }

        @Operation(summary = "update order")
        @PutMapping("/{id}")
        public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO dto) {
            return ResponseEntity.ok(orderService.updateOrder(id, dto));
        }

        @Operation(summary = "delete order")
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }

        @Operation(summary = "get all orders")
        @GetMapping
        public ResponseEntity<Page<OrderDTO>> getAllOrders(Pageable pageable) {
            return ResponseEntity.ok(orderService.getAllOrders(pageable));
        }

        @Operation(summary = "get order by id")
        @GetMapping("/{id}")
        public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
            return ResponseEntity.ok(orderService.getOrderById(id));
        }
    }


