package org.supplychain.supplychain.service.modelSupplier;

import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderDTO;

import java.util.List;

public interface SupplierOrderService {
    SupplyOrderDTO createOrder(SupplyOrderDTO dto);
    SupplyOrderDTO getOrderById(Long id);
    List<SupplyOrderDTO> getAllOrders();
    SupplyOrderDTO updateOrder(Long id, SupplyOrderDTO dto);
    void deleteOrder(Long id);
}
