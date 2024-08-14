package com.ehas.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

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
			return chain.filter(exchange);
		}
        return chain.filter(exchange);
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
