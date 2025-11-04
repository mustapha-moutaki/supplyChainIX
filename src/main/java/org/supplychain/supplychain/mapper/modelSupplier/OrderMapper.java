package org.supplychain.supplychain.mapper.modelSupplier;

import org.mapstruct.*;
import org.supplychain.supplychain.dto.order.OrderDTO;
import org.supplychain.supplychain.model.Order;


import java.util.ArrayList;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "id", source = "idOrder")
//    @Mapping(target = "productOrderIds",
//            expression = "java(order.getProductOrders() != null ? order.getProductOrders().stream().map(po -> po.getIdProductOrder()).collect(Collectors.toList()) : new ArrayList<>())")
    OrderDTO toDto(Order order);

    @InheritInverseConfiguration
    @Mapping(target = "productOrders", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    Order toEntity(OrderDTO dto);
}
