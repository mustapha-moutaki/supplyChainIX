package org.supplychain.supplychain.controller.Production;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.supplychain.supplychain.dto.productionorder.ProductionOrderDTO;
import org.supplychain.supplychain.enums.ProductionOrderStatus;
import org.supplychain.supplychain.response.SuccessResponse;
import org.supplychain.supplychain.service.Production.ProductionOrder.ProductionOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production-orders")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    @Operation(summary = "Create a new production order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Production order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductionOrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<ProductionOrderDTO>> createProductionOrder(
            @Valid @RequestBody ProductionOrderDTO dto,
            HttpServletRequest request) {

        ProductionOrderDTO createdOrder = productionOrderService.createProductionOrder(dto);
        SuccessResponse<ProductionOrderDTO> response = SuccessResponse.of(
                HttpStatus.CREATED,
                "Production order created successfully",
                createdOrder,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a production order by ID")
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductionOrderDTO>> updateProductionOrder(
            @PathVariable Long id,
            @Valid @RequestBody ProductionOrderDTO dto,
            HttpServletRequest request) {

        ProductionOrderDTO updatedOrder = productionOrderService.updateProductionOrder(id, dto);
        SuccessResponse<ProductionOrderDTO> response = SuccessResponse.of(
                HttpStatus.OK,
                "Production order updated successfully",
                updatedOrder,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancel a production order by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> cancelProductionOrder(
            @PathVariable Long id,
            HttpServletRequest request) {

        productionOrderService.cancelProductionOrder(id);
        SuccessResponse<Void> response = SuccessResponse.of(
                HttpStatus.OK,
                "Production order canceled successfully",
                null,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a production order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductionOrderDTO>> getProductionOrderById(
            @PathVariable Long id,
            HttpServletRequest request) {

        ProductionOrderDTO order = productionOrderService.getProductionOrderById(id);
        SuccessResponse<ProductionOrderDTO> response = SuccessResponse.of(
                HttpStatus.OK,
                "Production order retrieved successfully",
                order,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all production orders with pagination and sorting")
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
                "Production orders list retrieved successfully",
                orders,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get production orders filtered by status with pagination")
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
                "Production orders with status " + status + " retrieved successfully",
                orders,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Start production for a given production order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production started successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductionOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Production order not found"),
            @ApiResponse(responseCode = "400", description = "Not enough raw materials to start production")
    })
    @PutMapping("/production/{id}")
    public ResponseEntity<SuccessResponse<ProductionOrderDTO>> startProduction(
            @PathVariable Long id,
            HttpServletRequest request) {

        ProductionOrderDTO order = productionOrderService.production(id);

        SuccessResponse<ProductionOrderDTO> response = SuccessResponse.of(
                HttpStatus.OK,
                "Production started successfully",
                order,
                request.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }

}
