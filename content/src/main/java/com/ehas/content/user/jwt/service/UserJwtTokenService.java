package com.ehas.content.user.jwt.service;

import java.util.Date;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ehas.content.common.jwt.base.JwtTokenBase;
import com.ehas.content.common.jwt.dto.JwtToken;
import com.ehas.content.common.jwt.service.JwtRedisSerivceImpt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserJwtTokenService extends JwtTokenBase{
    public UserJwtTokenService(JwtRedisSerivceImpt JwtRedisSerivceImpt) {
    	super(JwtRedisSerivceImpt);
    }
	
	// AccessToken, RefreshToken 생성
    public JwtToken createJwtToken(Authentication authentication) {
    	JwtToken accessToken = super.createAccessToken(authentication);
    	JwtToken refreshToken = super.createRefreshToken(authentication);
    	
    	Boolean created = super.JwtRedisSerivceImpt.addRefreshToken(refreshToken.getRefreshTokenId(), Map.of("refreshToken",refreshToken.getRefreshToken()));
    	
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
    	log.info("Request Redis <- BlackToken : "+token);
        Date expiration = super.getPayloadToken(token).getExpiration();
        long now = new Date().getTime();
        long remainExpiration = expiration.getTime() - now;
        
        if(remainExpiration>0) {
        	return super.JwtRedisSerivceImpt.addBlacklistToken(token, remainExpiration);
        }
        return true;
    }
    
    // AccessToken 블랙리스트 존재여부
    public Boolean existsBlacklist(String token){
    	return super.JwtRedisSerivceImpt.existsBlacklistToken(token);
    }
}
