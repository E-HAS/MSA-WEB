package com.ehas.auth.User.jwt.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.ehas.auth.User.jwt.service.UserJwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/jwt/users/token")
@RequiredArgsConstructor
public class JwtRestController {
	private final UserJwtTokenProvider userJwtTokenProvider;

    @PostMapping("/refresh")
    public Mono<ResponseEntity<?>> refreshToken(ServerWebExchange exchange) {
    	return userJwtTokenProvider.validRefreshToken(exchange);
    }
}
