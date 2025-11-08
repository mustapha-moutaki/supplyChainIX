package org.supplychain.supplychain.dto.supplyOrder;

import lombok.Data;

import java.math.BigDecimal;

@Data

public class SupplyOrderLineDTO {
    private Long idLine;
    private Long supplyOrderId;
    private Long rawMaterialId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
