package org.supplychain.supplychain.service.modelSupplier.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderDTO;
import org.supplychain.supplychain.enums.SupplyOrderStatus;
import org.supplychain.supplychain.mapper.modelSupplier.SupplierOrderMapper;
import org.supplychain.supplychain.model.RawMaterial;
import org.supplychain.supplychain.model.Supplier;
import org.supplychain.supplychain.model.SupplyOrder;
import org.supplychain.supplychain.model.SupplyOrderLine;
import org.supplychain.supplychain.repository.approvisionnement.RawMaterialRepository;
import org.supplychain.supplychain.repository.approvisionnement.SupplierOrderRepository;
import org.supplychain.supplychain.repository.approvisionnement.SupplierRepository;
import org.supplychain.supplychain.service.modelSupplier.SupplierOrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplyOrderServiceImpl implements SupplierOrderService {

    private final SupplierOrderRepository repository;
    private final SupplierOrderMapper mapper;
    private final SupplierRepository supplierRepository;
    private final RawMaterialRepository rawMaterialRepository;

//    @Override
//    public SupplyOrderDTO createOrder(SupplyOrderDTO dto) {
//        SupplyOrder order = new SupplyOrder();
//
//        order.setOrderNumber(dto.getOrderNumber());
//        order.setTotalAmount(dto.getTotalAmount());
//        order.setStatus(dto.getStatus());
//
//        // default orderdate is current time
//        order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDate.now());
//
//        // get all suppliers
//        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
//                .orElseThrow(() -> new RuntimeException("Supplier not found"));
//        order.setSupplier(supplier);
//
//        SupplyOrder saved = repository.save(order);
//        return mapper.toDto(saved);
//    }
//
@Override
public SupplyOrderDTO createOrder(SupplyOrderDTO dto) {
    // Create a new SupplyOrder entity
    SupplyOrder order = new SupplyOrder();

    // ---------------------------
    // 1. Set default values if DTO fields are null
    // ---------------------------
    if (dto.getOrderNumber() == null) {
        // Example: "SO-1700000000000"
        dto.setOrderNumber("SO-" + System.currentTimeMillis());
    }
    if (dto.getStatus() == null) {
        // Default status: "EN_ATTENTE" (pending)
        dto.setStatus(SupplyOrderStatus.EN_ATTENTE);
    }

    order.setOrderNumber(dto.getOrderNumber());
    order.setStatus(dto.getStatus());

    // ---------------------------
    // 2. Set default order date to current date if null
    // ---------------------------
    order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDate.now());

    // ---------------------------
    // 3. Set Supplier
    // ---------------------------
    Supplier supplier = supplierRepository.findById(dto.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
    order.setSupplier(supplier);

    // ---------------------------
    // 4. Map order lines from DTO to entity
    // ---------------------------
    if (dto.getOrderLines() != null && !dto.getOrderLines().isEmpty()) {
        dto.getOrderLines().forEach(lineDTO -> {
            // Fetch the raw material from DB
            RawMaterial material = rawMaterialRepository.findById(lineDTO.getRawMaterialId())
                    .orElseThrow(() -> new RuntimeException("Raw material not found"));

            // ---------------------------
            // 4a. Check available stock before creating order line
            // ---------------------------
            int availableStock = material.getStock() - material.getReservedStock();
            // Example: stock = 3, reservedStock = 3, stockMin = 1 → availableStock = 0
            if (availableStock >= material.getStockMin()) {
                // Stock is sufficient, no need to order more
                // Stop creating this order line or throw exception
                throw new RuntimeException("Stock is sufficient for material: " + material.getName());
            }

            // ---------------------------
            // 4b. Create new SupplyOrderLine
            // ---------------------------
            SupplyOrderLine line = new SupplyOrderLine();
            line.setRawMaterial(material); // Link raw material
            line.setQuantity(lineDTO.getQuantity()); // Quantity requested
            line.setUnitPrice(material.getUnitPrice()); // Get unit price from material
            line.setSupplyOrder(order); // Link line to parent order

            // ---------------------------
            // 4c. Update reserved stock for this material
            // ---------------------------
            material.setReservedStock(material.getReservedStock() + lineDTO.getQuantity());
            rawMaterialRepository.save(material); // Save updated reservedStock

            // ---------------------------
            // 4d. Add this line to the order
            // ---------------------------
            order.getOrderLines().add(line);
        });
    }

    // ---------------------------
    // 5. Calculate total amount of the order
    // ---------------------------
    BigDecimal totalAmount = order.getOrderLines().stream()
            .map(l -> l.getUnitPrice().multiply(BigDecimal.valueOf(l.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    order.setTotalAmount(totalAmount);

    // ---------------------------
    // 6. Save order to repository
    // ---------------------------
    SupplyOrder saved = repository.save(order);

    // ---------------------------
    // 7. Return DTO
    // ---------------------------
    return mapper.toDto(saved);
}


    @Override
    public SupplyOrderDTO getOrderById(Long id) {
        SupplyOrder order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapper.toDto(order);
    }

    @Override
    public List<SupplyOrderDTO> getAllOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public SupplyOrderDTO updateOrder(Long id, SupplyOrderDTO dto) {
        SupplyOrder order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        SupplyOrder updated = mapper.updateEntity(order, dto);
        SupplyOrder saved = repository.save(updated);

        return mapper.toDto(saved);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        repository.deleteById(id);
    }
}
