package org.supplychain.supplychain.service.modelSupplier.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderDTO;
import org.supplychain.supplychain.mapper.modelSupplier.SupplierOrderMapper;
import org.supplychain.supplychain.model.Supplier;
import org.supplychain.supplychain.model.SupplyOrder;
import org.supplychain.supplychain.repository.approvisionnement.SupplierOrderRepository;
import org.supplychain.supplychain.repository.approvisionnement.SupplierRepository;
import org.supplychain.supplychain.service.modelSupplier.SupplierOrderService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplyOrderServiceImpl implements SupplierOrderService {

    private final SupplierOrderRepository repository;
    private final SupplierOrderMapper mapper;
    private final SupplierRepository supplierRepository;

    @Override
    public SupplyOrderDTO createOrder(SupplyOrderDTO dto) {
        SupplyOrder order = new SupplyOrder();

        order.setOrderNumber(dto.getOrderNumber());
        order.setTotalAmount(dto.getTotalAmount());
        order.setStatus(dto.getStatus());

        // default orderdate is current time
        order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDate.now());

        // get all suppliers
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        order.setSupplier(supplier);

        SupplyOrder saved = repository.save(order);
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
