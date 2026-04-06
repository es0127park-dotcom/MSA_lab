package com.metacoding.user.users;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private User(String username, String email, String password, String roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}