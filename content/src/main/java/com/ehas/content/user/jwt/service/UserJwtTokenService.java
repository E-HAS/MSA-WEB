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
    
    // AccessToken 재생성
    public String recreateAccessToken(String token){
	    	Date now = new Date();
	        Date expiryDate = new Date(now.getTime() + super.getAccessTokenexpirationTime());
			Claims payload = super.getPayloadToken(token);
			if(payload == null) {
				new Exception("Failed Recreate AccessToken");
			}
			return super.createTokenByPayload(payload, now, expiryDate);
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
    
    public ResponseEntity<?> validRefreshToken(HttpServletRequest request, HttpServletResponse response){
        
        // 1. 쿠키에서 refreshToken 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found in cookies.");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found in cookies.");
        }

        // 2. Redis에 존재하는지 확인
        if (!userJwtRedisSerivceImpt.existsRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .build()
            );
        }

        // 3. 토큰 유효성 검사
        try {
            if (!super.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        // 4. 새로운 액세스 토큰 재발급
        String accessTokenInHeader = super.resolveAccessToken(request);
        String recreatedAccessToken = this.recreateAccessToken(accessTokenInHeader);

        // 5. 응답 헤더에 액세스 토큰 추가
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + recreatedAccessToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.builder()
                        .status(HttpStatus.CREATED.value())
                        .message(HttpStatus.CREATED.getReasonPhrase())
                        .build());
    }
}
