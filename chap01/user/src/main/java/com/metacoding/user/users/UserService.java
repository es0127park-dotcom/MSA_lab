package com.metacoding.user.users;

import com.metacoding.user.core.handler.ex.*;
import com.metacoding.user.core.util.JwtUtil;
import static com.metacoding.user.core.util.JwtUtil.TOKEN_PREFIX;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String login(String username, String password) {
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception404("유저네임을 찾을 수 없습니다."));
        if (!findUser.getPassword().equals(password)) {
            throw new Exception401("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtUtil.create(findUser.getId(), findUser.getUsername());
        return TOKEN_PREFIX + token;
    }

    public UserResponse findById(int userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("회원 정보를 찾을 수 없습니다."));
        return UserResponse.from(findUser);
    }
}