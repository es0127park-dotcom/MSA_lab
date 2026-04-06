package com.metacoding.delivery.deliveries;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "delivery_tb")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderId;
    private String address;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private Delivery(int orderId, String address, DeliveryStatus status) {
        this.orderId = orderId;
        this.address = address;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    // 배달 생성
    public static Delivery create(int orderId, String address) {
        return new Delivery(orderId, address, DeliveryStatus.PENDING);
    }
    // 배달 완료
    public void complete() {
        this.status = DeliveryStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    // 배달 취소
    public void cancel() {
        this.status = DeliveryStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
}