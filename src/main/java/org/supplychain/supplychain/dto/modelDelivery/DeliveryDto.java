package org.supplychain.supplychain.dto.modelDelivery;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.supplychain.supplychain.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    private Long idDelivery;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotBlank(message = "Driver name is required")
    private String driver;

    @NotNull(message = "Delivery status is required")
    private DeliveryStatus status;

    @NotNull(message = "Delivery date is required")
    @FutureOrPresent(message = "Delivery date must be today or in the future")
    private LocalDate deliveryDate;

    @NotNull(message = "Delivery cost is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Delivery cost must be positive")
    private BigDecimal deliveryCost;
}
