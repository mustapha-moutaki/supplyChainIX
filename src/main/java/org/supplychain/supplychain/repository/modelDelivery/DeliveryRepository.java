package org.supplychain.supplychain.repository.modelDelivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.supplychain.supplychain.model.Delivery;
import org.supplychain.supplychain.model.Order;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    boolean existsByOrder(Order order);

}
