package com.metacoding.order.orders;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "order_item_tb")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private Long price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private OrderItem(int orderId, int productId, int quantity, Long price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    // 주문 아이템 생성
    public static OrderItem create(int orderId, int productId, int quantity, Long price) {
        return new OrderItem(orderId, productId, quantity, price);
    }
}