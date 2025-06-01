package com.ehas.content.user.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ehas.content.user.jwt.provider.UserJwtTokenProvider;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class UserJwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserJwtTokenProvider userJwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = userJwtTokenProvider.resolveAccessToken(request); // 1. Authorization 헤더에서 토큰 추출
            if (token != null && userJwtTokenProvider.validateToken(token)) { // 2. 토큰 유효성 검사
                Authentication authentication = userJwtTokenProvider.getAuthentication(token); // 3. 인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(authentication); // 4. SecurityContext에 설정
            }
            filterChain.doFilter(request, response); // 다음 필터 실행
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            onError(response, e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void onError(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        String responseBody = String.format("{\"status\":\"%s\", \"message\":\"%s\"}", statusCode, message);
        PrintWriter writer = response.getWriter();
        writer.write(responseBody);
        writer.flush();
    }
}
