package org.supplychain.supplychain.unit.impl.service.Production.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.supplychain.supplychain.dto.BOM.BillOfMaterialDTO;
import org.supplychain.supplychain.dto.product.ProductDTO;
import org.supplychain.supplychain.enums.OrderStatus;
import org.supplychain.supplychain.enums.ProductionOrderStatus;
import org.supplychain.supplychain.exception.DuplicateResourceException;
import org.supplychain.supplychain.exception.ResourceInUseException;
import org.supplychain.supplychain.exception.ResourceNotFoundException;
import org.supplychain.supplychain.mapper.Production.ProductMapper;
import org.supplychain.supplychain.model.BillOfMaterial;
import org.supplychain.supplychain.model.Product;
import org.supplychain.supplychain.model.RawMaterial;
import org.supplychain.supplychain.repository.Production.BillOfMaterialRepository;
import org.supplychain.supplychain.repository.Production.ProductRepository;
import org.supplychain.supplychain.repository.approvisionnement.RawMaterialRepository;
import org.supplychain.supplychain.service.Production.Product.ProductServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private BillOfMaterialRepository bomRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ==========================================
    //  TEST: createProduct Success
    // ==========================================
    @Test
    void testCreateProduct_Success() {

        ProductDTO dto = new ProductDTO();
        dto.setName("New Product");

        BillOfMaterialDTO bom = new BillOfMaterialDTO();
        bom.setMaterialId(1L);
        bom.setQuantity(5);
        dto.setBillOfMaterials(List.of(bom));

        Product productEntity = new Product();
        RawMaterial material = new RawMaterial();

        when(productRepository.findByName("New Product")).thenReturn(Optional.empty());
        when(productMapper.toEntity(dto)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(material));
        when(productMapper.toDTO(productEntity)).thenReturn(dto);

        ProductDTO result = productService.createProduct(dto);

        assertNotNull(result);
        verify(bomRepository, times(1)).saveAll(anyList());
    }

    // ==========================================
    // TEST: createProduct Duplicate Name
    // ==========================================
    @Test
    void testCreateProduct_DuplicateName() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Existing Product");

        when(productRepository.findByName("Existing Product"))
                .thenReturn(Optional.of(new Product()));

        assertThrows(DuplicateResourceException.class, () ->
                productService.createProduct(dto)
        );
    }

    // ==========================================
    // TEST: createProduct Missing BOM
    // ==========================================
    @Test
    void testCreateProduct_NoBOM() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Product");
        dto.setBillOfMaterials(null);

        assertThrows(IllegalArgumentException.class, () ->
                productService.createProduct(dto)
        );
    }

    // ==========================================
    // TEST: updateProduct Product Not Found
    // ==========================================
    @Test
    void testUpdateProduct_NotFound() {

        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                productService.updateProduct(10L, new ProductDTO())
        );
    }

    // ==========================================
    // TEST: deleteProduct Blocked by Orders
    // ==========================================
    @Test
    void testDeleteProduct_InUseByOrders() {

        Product product = new Product();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.countOrdersByProductIdAndStatus(1L, OrderStatus.EN_PREPARATION))
                .thenReturn(2L);

        assertThrows(ResourceInUseException.class, () ->
                productService.deleteProduct(1L)
        );
    }

    // ==========================================
    // TEST: getProductById Success
    // ==========================================
    @Test
    void testGetProductById_Success() {
        Product product = new Product();
        ProductDTO dto = new ProductDTO();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(dto);

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    // ==========================================
    // TEST: getProductById Not Found
    // ==========================================
    @Test
    void testGetProductById_NotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                productService.getProductById(1L)
        );
    }
}
