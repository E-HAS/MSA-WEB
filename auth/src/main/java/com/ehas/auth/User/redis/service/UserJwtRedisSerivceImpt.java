package com.ehas.auth.User.redis.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.ehas.auth.redis.service.CacheRedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserJwtRedisSerivceImpt {
	private final CacheRedisService cacheRedisService;
	
    private final String prefixRefreshToken= "refreshToken:";
	
	public Mono<Boolean> addRefreshToken(String token, Map<String, String> map){
		return cacheRedisService.save(prefixRefreshToken,token
														 ,map.toString(), Duration.ofDays(7));
	}
	
	public Mono<Boolean> deleteRefreshToken(String token){
		return cacheRedisService.delete(prefixRefreshToken,token);
	}
	
	public Mono<Boolean> existsRefreshToken(String token){
		return cacheRedisService.exists(prefixRefreshToken, token);
	}
	
}
