package com.ehas.auth.jwt.api;

import java.util.Map;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.auth.User.api.UserRestController;
import com.ehas.auth.User.service.UserServiceImpt;
import com.ehas.auth.jwt.dto.JwtUserDto;
import com.ehas.auth.jwt.service.UserJwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/jwt/token")
@RequiredArgsConstructor
public class JwtRestController {
	private final UserJwtTokenProvider userJwtTokenProvider;

    @PostMapping("/refresh")
    public Mono<ResponseEntity<?>> refreshToken(ServerHttpRequest request) {
        return Mono.justOrEmpty(
        		request.getCookies().getFirst("refreshToken"))
                .switchIfEmpty(Mono.error(new RuntimeException("Refresh token not found in cookies.")))
                .map(HttpCookie::getValue)
                .flatMap(refreshToken -> {
                	try {
	                    if (!userJwtTokenProvider.validateToken(refreshToken)) { // refreshToken 검증
	                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                                .body("Invalid refresh token."));
	                    }
                	} catch (Exception e) {
                		return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(e.getMessage()));
            		}
                	
                	String accessTokenInHeader = userJwtTokenProvider.resolveToken(request);
                	String recreateAccessToken = userJwtTokenProvider.recreateToken(accessTokenInHeader);
                    return Mono.just(ResponseEntity.ok(Map.of("accessToken", recreateAccessToken)));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(e.getMessage())));
    }
}
