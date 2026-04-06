package com.metacoding.product.domain;

import com.metacoding.product.core.handler.ex.Exception400;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "product_tb")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productName;
    private int quantity;
    private Long price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private Product(String productName, int quantity, Long price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // 재고 감소
    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    // 재고 증가
    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    // 재고 부족 검증
    public void validateQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new Exception400("상품 재고가 부족합니다.");
        }
    }
    
    // 가격 일치 검증
    public void validatePrice(Long price) {
        if (!price.equals(this.price)) {
            throw new Exception400("상품 가격이 일치하지 않습니다.");
        }
    }
}
