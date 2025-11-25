package org.supplychain.supplychain.dto.supplyOrder;

import lombok.Data;
import org.supplychain.supplychain.enums.SupplyOrderStatus;
import org.supplychain.supplychain.model.SupplyOrderLine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SupplyOrderDTO {

    private String orderNumber;
    private Long supplierId;
    private List<SupplyOrderLineDTO> orderLines;
    private LocalDate orderDate;
    private SupplyOrderStatus status;
    private BigDecimal totalAmount;
}
