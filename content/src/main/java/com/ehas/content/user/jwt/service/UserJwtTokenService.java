package com.ehas.content.user.jwt.service;

import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ehas.content.user.dto.ResponseDto;
import com.ehas.content.user.jwt.base.JwtTokenBase;
import com.ehas.content.user.jwt.dto.JwtToken;
import com.ehas.content.user.redis.service.UserJwtRedisSerivceImpt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserJwtTokenService extends JwtTokenBase{
	private final UserJwtRedisSerivceImpt userJwtRedisSerivceImpt;
	
    public UserJwtTokenService(UserDetailsService userDetailsService, UserJwtRedisSerivceImpt userJwtRedisSerivceImpt) {
        super(userDetailsService);
        this.userJwtRedisSerivceImpt = userJwtRedisSerivceImpt;
    }
	
	// AccessToken, RefreshToken 생성
    public JwtToken createJwtToken(Authentication authentication) {
    	JwtToken accessToken = super.createAccessToken(authentication);
    	JwtToken refreshToken = super.createRefreshToken(authentication);
    	
    	Boolean created = userJwtRedisSerivceImpt.addRefreshToken(refreshToken.getRefreshTokenId(), Map.of("refreshToken",refreshToken.getRefreshToken()));
    	
    	if(!created) {
    		new RuntimeException("Failed to store tokens in Redis");
    	}
    	
    	return JwtToken.builder()
	    			.prefix(accessToken.getPrefix())
	    			.accessToken(accessToken.getAccessToken())
	    			.refreshToken(refreshToken.getRefreshToken())
	    			.build();
    }
    
    // AccessToken 블랙리스트 등록
    public Boolean addBlacklist(String token){
        Date expiration = super.getPayloadToken(token).getExpiration();
        long now = new Date().getTime();
        long remainExpiration = expiration.getTime() - now;
        
        if(remainExpiration>0) {
        	return userJwtRedisSerivceImpt.addBlacklistToken(token, remainExpiration);
        }
        return true;
    }
    
    // AccessToken 블랙리스트 존재여부
    public Boolean existsBlacklist(String token){
    	return userJwtRedisSerivceImpt.existsBlacklistToken(token);
    }
}
