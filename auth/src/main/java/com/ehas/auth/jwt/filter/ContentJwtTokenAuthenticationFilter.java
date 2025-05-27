package com.ehas.auth.jwt.filter;

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

import com.ehas.auth.jwt.service.ContentJwtTokenProvider;

import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ContentJwtTokenAuthenticationFilter implements WebFilter  {
	public static final String HEADER_PREFIX = "Bearer ";
    private final ContentJwtTokenProvider contentJwtTokenProvider;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    	
        try {
            String token = this.resolveToken(exchange.getRequest());
			if(StringUtils.hasText(token) && this.contentJwtTokenProvider.validateToken(token)) {
			    Authentication authentication = this.contentJwtTokenProvider.getAuthentication(token);
			    return chain.filter(exchange)
			            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
			}
		} catch (Exception e) {
			return onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
        return chain.filter(exchange);
    }
    
    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
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
