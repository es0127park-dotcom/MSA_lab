package com.metacoding.order.orders;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "order_tb")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private Order(int userId,  OrderStatus status) {
        this.userId = userId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    // 주문 생성
    public static Order create(int userId) {
        return new Order(userId, OrderStatus.PENDING);
    }
    // 주문 완료
    public void complete() {
        this.status = OrderStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    // 주문 취소
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

}