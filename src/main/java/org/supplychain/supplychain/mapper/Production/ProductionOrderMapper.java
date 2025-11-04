package org.supplychain.supplychain.mapper.Production;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.supplychain.supplychain.dto.productionorder.ProductionOrderDTO;
import org.supplychain.supplychain.model.ProductionOrder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductionOrderMapper {

    @Mapping(target = "idOrder", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ProductionOrder toEntity(ProductionOrderDTO dto);

    @Mapping(source = "product.idProduct", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    ProductionOrderDTO toDTO(ProductionOrder entity);

    @Mapping(target = "idOrder", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDTO(ProductionOrderDTO dto, @MappingTarget ProductionOrder entity);
}