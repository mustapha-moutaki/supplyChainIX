package org.supplychain.supplychain.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.supplychain.supplychain.dto.modelDelivery.CustomerDto;
//import org.supplychain.supplychain.dto.delivery.DeliveryDTO;
//import org.supplychain.supplychain.dto.product.ProductOrderDTO;
import org.supplychain.supplychain.enums.OrderStatus;
import org.supplychain.supplychain.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private Long customerId;

    private List<Product> productOrders = new ArrayList<>();

    private BigDecimal totalAmount;

    private OrderStatus status;

//    private DeliveryDTO delivery;

}
