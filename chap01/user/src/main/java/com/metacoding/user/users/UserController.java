package com.metacoding.user.users;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.metacoding.user.core.util.Resp;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest requestDTO) {
        return Resp.ok(userService.login(requestDTO.username(), requestDTO.password()));
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") int userId) {
        return Resp.ok(userService.findById(userId));
    }
}
