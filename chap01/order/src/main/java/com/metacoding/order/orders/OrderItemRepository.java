package com.metacoding.order.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Optional<List<OrderItem>> findByOrderId(int orderId);
}