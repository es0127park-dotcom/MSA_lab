package com.metacoding.user.usecase;

import com.metacoding.user.web.dto.UserResponse;

import java.util.List;

public interface GetAllUsersUseCase {
    List<UserResponse> findAll();
}
