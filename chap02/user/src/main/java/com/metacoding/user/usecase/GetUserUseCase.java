package com.metacoding.user.usecase;

import com.metacoding.user.web.dto.UserResponse;

public interface GetUserUseCase {
    UserResponse findById(int userId);
}
