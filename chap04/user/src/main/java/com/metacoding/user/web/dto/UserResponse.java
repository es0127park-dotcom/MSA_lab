package com.metacoding.user.web.dto;

import com.metacoding.user.domain.User;

public record UserResponse(
    int id,
    String username,
    String email
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }
}
