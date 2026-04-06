package com.metacoding.user.core.util;

import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    public static final String HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    
    private final String secret;
    private final Long expirationTime;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration:86400000}") Long expirationTime) {
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    // JWT 토큰 생성
    public String create(int userId, String username) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(secret));
    }

    // JWT 토큰 검증 및 디코딩
    public DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token);
    }

    // 토큰에서 사용자 ID 추출
    public int getUserId(String token) {
        DecodedJWT decodedJWT = verify(token);
        return Integer.parseInt(decodedJWT.getSubject());
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}