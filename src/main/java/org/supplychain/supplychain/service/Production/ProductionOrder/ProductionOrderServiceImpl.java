package org.supplychain.supplychain.service.Production.ProductionOrder;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supplychain.supplychain.dto.productionorder.ProductionOrderDTO;
import org.supplychain.supplychain.enums.Priority;
import org.supplychain.supplychain.enums.ProductionOrderStatus;
import org.supplychain.supplychain.exception.DuplicateResourceException;
import org.supplychain.supplychain.exception.ResourceInUseException;
import org.supplychain.supplychain.exception.ResourceNotFoundException;
import org.supplychain.supplychain.mapper.Production.ProductionOrderMapper;
import org.supplychain.supplychain.model.Product;
import org.supplychain.supplychain.model.ProductionOrder;
import org.supplychain.supplychain.repository.Production.ProductRepository;
import org.supplychain.supplychain.repository.Production.ProductionOrderRepository;

@Service
@RequiredArgsConstructor
public class ProductionOrderServiceImpl implements ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final ProductRepository productRepository;
    private final ProductionOrderMapper productionOrderMapper;

    @Override
    @Transactional
    public ProductionOrderDTO createProductionOrder(ProductionOrderDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID : " + dto.getProductId()));

        if (productionOrderRepository.existsByOrderNumber(dto.getOrderNumber())) {
            throw new DuplicateResourceException(
                    "Un ordre avec le numéro '" + dto.getOrderNumber() + "' existe déjà");
        }

        ProductionOrder productionOrder = productionOrderMapper.toEntity(dto);
        productionOrder.setProduct(product);


        if (productionOrder.getStatus() == null) {
            productionOrder.setStatus(ProductionOrderStatus.EN_ATTENTE);
        }

        if (productionOrder.getPriority() == null) {
            productionOrder.setPriority(Priority.STANDARD);
        }

        ProductionOrder savedOrder = productionOrderRepository.save(productionOrder);

        return productionOrderMapper.toDTO(savedOrder);
    }

    @Override
    @Transactional
    public ProductionOrderDTO updateProductionOrder(Long id, ProductionOrderDTO dto) {
        ProductionOrder existingOrder = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ordre de production non trouvé avec l'ID : " + id));

        if (!existingOrder.getOrderNumber().equals(dto.getOrderNumber()) &&
                productionOrderRepository.existsByOrderNumber(dto.getOrderNumber())) {
            throw new DuplicateResourceException(
                    "Un ordre avec le numéro '" + dto.getOrderNumber() + "' existe déjà");
        }

        if (dto.getProductId() != null &&
                !dto.getProductId().equals(existingOrder.getProduct().getIdProduct())) {

            Product newProduct = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Produit non trouvé avec l'ID : " + dto.getProductId()));

            existingOrder.setProduct(newProduct);
        }

        productionOrderMapper.updateEntityFromDTO(dto, existingOrder);

        ProductionOrder updatedOrder = productionOrderRepository.save(existingOrder);

        return productionOrderMapper.toDTO(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelProductionOrder(Long id) {
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ordre de production non trouvé avec l'ID : " + id));

        if (order.getStatus() != ProductionOrderStatus.EN_ATTENTE) {
            throw new ResourceInUseException(
                    "Impossible d'annuler l'ordre. Seuls les ordres avec le statut EN_ATTENTE peuvent être annulés. " +
                            "Statut actuel : " + order.getStatus()
            );
        }

        productionOrderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductionOrderDTO> getAllProductionOrders(Pageable pageable) {
        return productionOrderRepository.findAll(pageable)
                .map(productionOrderMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductionOrderDTO> getProductionOrdersByStatus(
            ProductionOrderStatus status,
            Pageable pageable) {

        return productionOrderRepository.findByStatus(status, pageable)
                .map(productionOrderMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductionOrderDTO getProductionOrderById(Long id) {
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ordre de production non trouvé avec l'ID : " + id));

        return productionOrderMapper.toDTO(order);
    }
}
