package org.supplychain.supplychain.service.modelSupplier;

import org.springframework.data.domain.Page;
import org.supplychain.supplychain.dto.order.OrderDTO;


import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderServiec {
    OrderDTO createOrder(OrderDTO dto);
    OrderDTO updateOrder(Long id, OrderDTO dto);
    void deleteOrder(Long id);
    Page<OrderDTO> getAllOrders(Pageable pageable);
    OrderDTO getOrderById(Long id);
}
