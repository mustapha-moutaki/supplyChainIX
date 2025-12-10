package org.supplychain.supplychain.mapper.modelSupplier;

import org.mapstruct.*;
import org.supplychain.supplychain.dto.order.OrderDTO;
import org.supplychain.supplychain.mapper.Production.ProductOrderMapper;
import org.supplychain.supplychain.model.Order;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductOrderMapper.class}
)
public interface OrderMapper {

    @Mapping(target = "id", source = "idOrder")
    @Mapping(target = "customerId", source = "customer.idCustomer")  // FIXED
    @Mapping(target = "productOrders", source = "productOrders")
    OrderDTO toDto(Order order);

    @InheritInverseConfiguration
    @Mapping(target = "customer", ignore = true)       // set in service
    @Mapping(target = "productOrders", ignore = true)  // set in service
    @Mapping(target = "delivery", ignore = true)
    Order toEntity(OrderDTO dto);
}
