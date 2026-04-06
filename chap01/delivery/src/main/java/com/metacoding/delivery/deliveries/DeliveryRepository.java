package com.metacoding.delivery.deliveries;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    Optional<Delivery> findByOrderId(int orderId);
}