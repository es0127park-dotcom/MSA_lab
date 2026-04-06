package com.metacoding.order.domain;

import com.metacoding.order.core.handler.ex.Exception400;
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
        validateCancelable();
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    // 주문 취소 가능 여부 검증
    public void validateCancelable() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new Exception400("주문이 이미 취소되었습니다.");
        }
    }

}
