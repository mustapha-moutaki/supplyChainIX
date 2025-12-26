package org.supplychain.supplychain.controller.approvisionnement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 1. Added for logging
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.supplychain.supplychain.constants.supplierConstants.ApiConstants;
import org.supplychain.supplychain.dto.supplier.SupplierDTO;
import org.supplychain.supplychain.service.modelSupplier.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.API + ApiConstants.SUPPLIER_ENDPOINT)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supplier Controller", description = "Manage suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "Create a new supplier")
    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@Valid @RequestBody SupplierDTO dto) {
        log.info("REST request to create a new Supplier: {}", dto.getName());
        SupplierDTO response = supplierService.createSupplier(dto);
        log.info("Supplier created successfully with ID: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an existing supplier by ID")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierDTO dto) {
        log.info("REST request to update Supplier ID: {}", id);
        SupplierDTO response = supplierService.updateSupplier(id, dto);
        log.info("Supplier ID: {} updated successfully", id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a supplier by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        log.warn("REST request to DELETE Supplier ID: {}", id);
        try {
            supplierService.deleteSupplier(id);
            log.info("Supplier ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Conflict error while deleting Supplier ID: {}. Reason: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Get all suppliers with pagination")
    @GetMapping
    public ResponseEntity<Page<SupplierDTO>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("REST request to get all Suppliers - page: {}, size: {}", page, size);
        Page<SupplierDTO> response = supplierService.getAllSuppliers(page, size);
        log.debug("Found {} total suppliers in database", response.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search suppliers by name")
    @GetMapping("/search")
    public ResponseEntity<List<SupplierDTO>> searchSupplier(@RequestParam String name) {
        log.info("REST request to search Suppliers by name containing: {}", name);
        List<SupplierDTO> response = supplierService.searchSupplierByName(name);
        log.info("Search returned {} results", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a supplier by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long id) {
        log.info("REST request to get Supplier details for ID: {}", id);
        SupplierDTO supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }
}