package com.metacoding.user.usecase;

import com.metacoding.user.core.handler.ex.*;
import com.metacoding.user.core.util.JwtUtil;
import com.metacoding.user.domain.User;
import com.metacoding.user.repository.UserRepository;
import com.metacoding.user.web.dto.UserResponse;
import static com.metacoding.user.core.util.JwtUtil.TOKEN_PREFIX;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService implements GetUserUseCase, GetAllUsersUseCase, LoginUseCase {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse findById(int userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("회원 정보를 찾을 수 없습니다."));
        return UserResponse.from(findUser);
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public String login(String username, String password) {
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception404("유저네임을 찾을 수 없습니다."));
        // 비밀번호 검증
        findUser.validatePassword(password);
        String token = jwtUtil.create(findUser.getId(), findUser.getUsername());
        return TOKEN_PREFIX + token;
    }
}
