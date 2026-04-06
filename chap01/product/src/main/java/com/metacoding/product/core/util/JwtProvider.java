package com.metacoding.product.core.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private final JwtUtil jwtUtil;

    // 요청 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtUtil.HEADER);
        if (bearerToken != null && bearerToken.startsWith(JwtUtil.TOKEN_PREFIX)) {
            return bearerToken.replace(JwtUtil.TOKEN_PREFIX, "");
        }
        return null;
    }

    // 토큰에서 사용자 ID 추출
    public Integer getUserId(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            return jwtUtil.getUserId(token);
        }
        return null;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
