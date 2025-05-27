package com.ehas.auth.jwt.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.ehas.auth.jwt.service.UserJwtTokenProvider;

import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class UserJwtTokenAuthenticationFilter implements WebFilter  {
    private final UserJwtTokenProvider userJwtTokenProvider;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    	
        try {
            String token = userJwtTokenProvider.resolveToken(exchange.getRequest());  // Header(Bearer)에서 토큰 가져오기
			if(StringUtils.hasText(token) && this.userJwtTokenProvider.validateToken(token)) {  // 토큰 검증
			    Authentication authentication = this.userJwtTokenProvider.getAuthentication(token); // Authentication 생성
			    return chain.filter(exchange)
			            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)); // reactor context에 Authentication 등록
			}
		} catch (Exception e) {
			return onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
        return chain.filter(exchange);
    }
    
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        Map<String, String> responseBody = Map.of(	"status",httpStatus.toString()
        											,"message",err.toString());

        byte[] bytes = responseBody.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
