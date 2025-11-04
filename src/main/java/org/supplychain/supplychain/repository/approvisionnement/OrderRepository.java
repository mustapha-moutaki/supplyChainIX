package org.supplychain.supplychain.repository.approvisionnement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.supplychain.supplychain.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
