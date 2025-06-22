package com.ehas.lotto.common.user.filter;

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

import com.ehas.lotto.common.user.provider.UserJwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class UserJwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserJwtTokenProvider JwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = JwtTokenProvider.resolveAccessToken(request); // 1. Authorization 헤더에서 토큰 추출
            if (token != null && JwtTokenProvider.validateToken(token)) { // 2. 토큰 유효성 검사
            	if(JwtTokenProvider.existsBlacklist(token)) {             // 3. 블랙리스트 여부 검사
            		Authentication authentication = JwtTokenProvider.getAuthentication(token); // 4. 인증 객체 생성
                    SecurityContextHolder.getContext().setAuthentication(authentication); // 5. SecurityContext에 설정
            	}
            }
            filterChain.doFilter(request, response); // 다음 필터 실행
        }
        catch (Exception e) {
            log.error("User AuthenticationFilter Error: {}", e.getMessage());
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
