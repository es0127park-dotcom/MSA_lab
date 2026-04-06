package com.metacoding.delivery.domain;

import com.metacoding.delivery.core.handler.ex.Exception400;
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
    // 배달 주소 검증
    public static void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new Exception400("배달 주소는 필수입니다.");
        }
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
        validateCancelable();
        this.status = DeliveryStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    // 배달 취소 가능 여부 검증
    public void validateCancelable() {
        if (this.status == DeliveryStatus.CANCELLED) {
            throw new Exception400("배달이 이미 취소되었습니다.");
        }
    }
}
