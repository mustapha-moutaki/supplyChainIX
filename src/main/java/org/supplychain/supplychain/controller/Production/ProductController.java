package org.supplychain.supplychain.controller.Production;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.supplychain.supplychain.dto.product.ProductDTO;
import org.supplychain.supplychain.response.SuccessResponse;
import org.supplychain.supplychain.service.Production.Product.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<ProductDTO>> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            HttpServletRequest request) {

        ProductDTO createdProduct = productService.createProduct(productDTO);
        SuccessResponse<ProductDTO> response = SuccessResponse.of(
                HttpStatus.CREATED,
                "Product created successfully",
                createdProduct,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a product by ID")
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO,
            HttpServletRequest request) {

        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        SuccessResponse<ProductDTO> response = SuccessResponse.of(
                HttpStatus.OK,
                "Product updated successfully",
                updatedProduct,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteProduct(
            @PathVariable Long id,
            HttpServletRequest request) {

        productService.deleteProduct(id);
        SuccessResponse<Void> response = SuccessResponse.of(
                HttpStatus.OK,
                "Product deleted successfully",
                null,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductDTO>> getProductById(
            @PathVariable Long id,
            HttpServletRequest request) {

        ProductDTO product = productService.getProductById(id);
        SuccessResponse<ProductDTO> response = SuccessResponse.of(
                HttpStatus.OK,
                "Product retrieved successfully",
                product,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all products with pagination and sorting")
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<ProductDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            HttpServletRequest request) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductDTO> products = productService.getAllProducts(pageable);

        SuccessResponse<Page<ProductDTO>> response = SuccessResponse.of(
                HttpStatus.OK,
                "Products list retrieved successfully",
                products,
                request.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }
}
