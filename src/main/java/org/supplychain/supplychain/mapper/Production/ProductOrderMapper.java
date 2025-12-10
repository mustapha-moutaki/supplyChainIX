package org.supplychain.supplychain.mapper.Production;

import org.mapstruct.*;
import org.supplychain.supplychain.dto.order.ProductOrderDTO;
import org.supplychain.supplychain.model.ProductOrder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductOrderMapper {

    @Mapping(target = "productId", source = "product.id")
    ProductOrderDTO toDto(ProductOrder productOrder);

    @InheritInverseConfiguration
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductOrder toEntity(ProductOrderDTO dto);
}
