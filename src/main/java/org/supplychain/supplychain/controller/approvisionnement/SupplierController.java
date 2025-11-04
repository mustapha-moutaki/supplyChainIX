package org.supplychain.supplychain.controller.approvisionnement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.supplychain.supplychain.constants.supplierConstants.ApiConstants;

import org.supplychain.supplychain.dto.supplier.SupplierDTO;
import org.supplychain.supplychain.service.modelSupplier.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//@RequestMapping("/api/suppliers")
@RequestMapping(ApiConstants.API+ApiConstants.SUPPLIER_ENDPOINT)
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@Valid @RequestBody SupplierDTO dto) {
        return ResponseEntity.ok(supplierService.createSupplier(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierDTO dto) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, dto));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
//        supplierService.deleteSupplier(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.noContent().build(); //delete
        } catch (RuntimeException e) {
            //message for the user
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<Page<SupplierDTO>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(page, size));
    }


    @GetMapping("/search")
    public ResponseEntity<List<SupplierDTO>> searchSupplier(@RequestParam String name) {
        return ResponseEntity.ok(supplierService.searchSupplierByName(name));
    }
}
