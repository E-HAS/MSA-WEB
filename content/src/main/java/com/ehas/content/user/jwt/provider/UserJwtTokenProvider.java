package com.ehas.content.user.jwt.provider;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.ehas.content.user.jwt.base.JwtTokenBase;
import com.ehas.content.user.redis.service.UserJwtRedisSerivceImpt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class UserJwtTokenProvider extends JwtTokenBase{
	private final UserJwtRedisSerivceImpt userJwtRedisSerivceImpt;
	
    public UserJwtTokenProvider(UserDetailsService userDetailsService, UserJwtRedisSerivceImpt userJwtRedisSerivceImpt) {
        super(userDetailsService);
        this.userJwtRedisSerivceImpt = userJwtRedisSerivceImpt;
    }
    
    @Override
    public String resolveAccessToken(HttpServletRequest request) {
    	return super.resolveAccessToken(request);
    }

    @Override
    public boolean validateToken(String token) throws Exception {
    	return super.validateToken(token);
    }
    
    @Override
    public Authentication getAuthentication(String token) {
        return super.getAuthentication(token);
    }
    
    // AccessToken 재생성
    @Override
    public String recreateAccessToken(String token){
			return super.recreateAccessToken(token);
    }
    
    public String validdateRefreshToken(HttpServletRequest request, HttpServletResponse response){
        // 1. 쿠키에서 refreshToken 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
        	new Exception("Refresh token not found in cookies.");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
        	new Exception("Refresh token not found in cookies.");
        }

        // 2. Redis에 존재하는지 확인
        if (!userJwtRedisSerivceImpt.existsRefreshToken(refreshToken)) {
        	new Exception(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        // 3. 토큰 유효성 검사
        try {
            if (!super.validateToken(refreshToken)) {
            	new Exception("Invalid refresh token.");
            }
        } catch (Exception e) {
        	new Exception(e.getMessage());
        }
        // 4. 새로운 액세스 토큰 재발급
        String accessTokenInHeader = super.resolveAccessToken(request);
        String recreatedAccessToken = super.recreateAccessToken(accessTokenInHeader);

        // 5. 응답 헤더에 액세스 토큰 추가
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + recreatedAccessToken);
        
        return recreatedAccessToken;
    }

    // AccessToken 블랙리스트 존재여부
    public Boolean existsBlacklist(String token) throws Exception {
    	try {
    		if(userJwtRedisSerivceImpt.existsBlacklistToken(token)) {
    			return true;
    		}else {
    			throw new Exception("Invalid JWT token");
    		}
    	}catch (Exception e) {
    		throw new Exception("Invalid JWT token");
    	}
    }
}