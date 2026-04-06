package com.metacoding.product.products;

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
}