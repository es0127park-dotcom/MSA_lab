package com.metacoding.product.core.filter;

import com.metacoding.product.core.util.JwtProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        // 토큰 인증만 수행 (X-User-Id 헤더 검증 제거)
        String token = jwtProvider.resolveToken(request);
        
        if (token == null || !jwtProvider.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다");
            return;
        }
        
        Integer userId = jwtProvider.getUserId(request);
        if (userId != null) {
            request.setAttribute("userId", userId);
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다");
            return;
        }
    }
		// 인증을 하지 않을 경로 
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/login") ||
               path.startsWith("/h2-console");
    }
}