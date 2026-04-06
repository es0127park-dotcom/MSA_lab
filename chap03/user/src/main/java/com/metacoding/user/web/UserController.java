package com.metacoding.user.web;

import com.metacoding.user.usecase.*;
import com.metacoding.user.web.dto.UserRequest;
import com.metacoding.user.core.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final GetUserUseCase getUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest requestDTO) {
        return Resp.ok(loginUseCase.login(requestDTO.username(), requestDTO.password()));
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") int userId) {
        return Resp.ok(getUserUseCase.findById(userId));
    }

    @GetMapping("/api/users")
    public ResponseEntity<?> getAllUsers() {
        return Resp.ok(getAllUsersUseCase.findAll());
    }
}
