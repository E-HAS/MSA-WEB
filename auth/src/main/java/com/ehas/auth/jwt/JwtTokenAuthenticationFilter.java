package com.ehas.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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

import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter  {
	public static final String HEADER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    	
        try {
            String token = resolveToken(exchange.getRequest());
            System.out.println(">>>> filter before " + token);
			if(StringUtils.hasText(token) && this.jwtTokenProvider.validateToken(token)) {
			    Authentication authentication = this.jwtTokenProvider.getAuthentication(token);
			    System.out.println(">>>> filter Authentication " + authentication.toString());
			    
			    return chain.filter(exchange)
			            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
			    
			}
		} catch (Exception e) {
			/*
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.BAD_REQUEST);
			
			return response.setComplete();
			*/
			return onError(exchange, "UNAUHORIZATION", HttpStatus.UNAUTHORIZED);
		}
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        Map<String, String> responseBody = Map.of(
        										"code",httpStatus.toString()
        										,"msg",err.toString());

        byte[] bytes = responseBody.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
		// 요청으로부터 JWT 토큰을 얻는다.
    private String resolveToken(ServerHttpRequest request) {
    	System.out.println(">>>> reslove before " + request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
