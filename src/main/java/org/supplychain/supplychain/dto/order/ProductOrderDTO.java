package org.supplychain.supplychain.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDTO {

    private long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;


}
