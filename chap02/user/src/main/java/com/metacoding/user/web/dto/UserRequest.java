package com.metacoding.user.web.dto;

public record UserRequest(
    String username,
    String password
) {
}
