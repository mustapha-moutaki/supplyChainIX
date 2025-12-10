package org.supplychain.supplychain.controller.modelDelivery;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supplychain.supplychain.dto.modelDelivery.DeliveryDto;
import org.supplychain.supplychain.service.modelDelivery.interfaces.IDeliveryService;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final IDeliveryService deliveryService;

    @Operation(summary = "Create a new delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeliveryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<DeliveryDto> createDelivery(@RequestBody DeliveryDto dto) {
        DeliveryDto created = deliveryService.createDelivery(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Retrieve all deliveries")
    @GetMapping
    public ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    @Operation(summary = "Retrieve a delivery by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }

    @Operation(summary = "Update a delivery by ID")
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDto> updateDelivery(@PathVariable Long id, @RequestBody DeliveryDto dto) {
        return ResponseEntity.ok(deliveryService.updateDelivery(id, dto));
    }

    @Operation(summary = "Delete a delivery by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
}
