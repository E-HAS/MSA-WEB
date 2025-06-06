package com.ehas.content.common.jwt.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ehas.content.common.redis.service.CacheRedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtRedisSerivceImpt {
	private final CacheRedisService cacheRedisService;
	
    private final String prefixRefreshToken= "refreshToken:";
    private final Integer durationOfDay= 7;
    
    private final String prefixBlacklistToken= "blacklistToken:";
    private final Integer durationOfMin= 60;
	
	public Boolean addRefreshToken(String token, Map<String, String> map){
		return cacheRedisService.save(prefixRefreshToken,token
														 ,map.toString(), Duration.ofDays(durationOfDay));
	}
	
	public Boolean deleteRefreshToken(String token){
		return cacheRedisService.delete(prefixRefreshToken,token);
	}
	
	public Boolean existsRefreshToken(String token){
		return cacheRedisService.exists(prefixRefreshToken, token);
	}
	
    // accessToken -> blacklist 저장
    public Boolean addBlacklistToken(String accessToken, long remain) {
        return cacheRedisService.save(prefixBlacklistToken,accessToken,""
        												,Duration.ofMillis(remain));
    }
    // blacklist <- accessToken 존재 여부 확인
    public Boolean existsBlacklistToken(String accessToken) {
        return cacheRedisService.exists(prefixBlacklistToken, accessToken);
    }
}
