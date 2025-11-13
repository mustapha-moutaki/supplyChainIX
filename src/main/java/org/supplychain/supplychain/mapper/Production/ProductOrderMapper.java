package org.supplychain.supplychain.mapper.Production;


import org.mapstruct.*;
import org.supplychain.supplychain.dto.order.ProductOrderDTO;
import org.supplychain.supplychain.model.ProductOrder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductOrderMapper {

    @Mapping(target = "productId", source = "product.idProduct")
    ProductOrderDTO toDto(ProductOrder productOrder);

    @InheritInverseConfiguration
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "order", ignore = true)
    ProductOrder toEntity(ProductOrderDTO dto);

}