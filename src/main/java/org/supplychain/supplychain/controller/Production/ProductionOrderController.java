package org.supplychain.supplychain.controller.Production;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supplychain.supplychain.dto.productionorder.ProductionOrderDTO;
import org.supplychain.supplychain.enums.ProductionOrderStatus;
import org.supplychain.supplychain.response.SuccessResponse;
import org.supplychain.supplychain.service.Production.ProductionOrder.ProductionOrderService;


@RestController
@RequestMapping("/api/production-orders")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    @PostMapping
    public ResponseEntity<SuccessResponse<ProductionOrderDTO>> createProductionOrder(
            @Valid @RequestBody ProductionOrderDTO dto,
            HttpServletRequest request) {

        ProductionOrderDTO createdOrder = productionOrderService.createProductionOrder(dto);
        SuccessResponse<ProductionOrderDTO> response = SuccessResponse.of(
                HttpStatus.CREATED,
                "Ordre de production créé avec succès",
                createdOrder,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductionOrderDTO>> updateProductionOrder(
            @PathVariable Long id,
            @Valid @RequestBody ProductionOrderDTO dto,
            HttpServletRequest request) {

        ProductionOrderDTO updatedOrder = productionOrderService.updateProductionOrder(id, dto);
        SuccessResponse<ProductionOrderDTO> response = SuccessResponse.of(
                HttpStatus.OK,
                "Ordre de production mis à jour avec succès",
                updatedOrder,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> cancelProductionOrder(
            @PathVariable Long id,
            HttpServletRequest request) {

        productionOrderService.cancelProductionOrder(id);
        SuccessResponse<Void> response = SuccessResponse.of(
                HttpStatus.OK,
                "Ordre de production annulé avec succès",
                null,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductionOrderDTO>> getProductionOrderById(
            @PathVariable Long id,
            HttpServletRequest request) {

        ProductionOrderDTO order = productionOrderService.getProductionOrderById(id);
        SuccessResponse<ProductionOrderDTO> response = SuccessResponse.of(
                HttpStatus.OK,
                "Ordre de production récupéré avec succès",
                order,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<SuccessResponse<Page<ProductionOrderDTO>>> getAllProductionOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idOrder") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            HttpServletRequest request) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductionOrderDTO> orders = productionOrderService.getAllProductionOrders(pageable);

        SuccessResponse<Page<ProductionOrderDTO>> response = SuccessResponse.of(
                HttpStatus.OK,
                "Liste des ordres de production récupérée avec succès",
                orders,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<SuccessResponse<Page<ProductionOrderDTO>>> getProductionOrdersByStatus(
            @PathVariable ProductionOrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("idOrder").descending());
        Page<ProductionOrderDTO> orders = productionOrderService.getProductionOrdersByStatus(status, pageable);

        SuccessResponse<Page<ProductionOrderDTO>> response = SuccessResponse.of(
                HttpStatus.OK,
                "Ordres avec statut " + status + " récupérés avec succès",
                orders,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }
}