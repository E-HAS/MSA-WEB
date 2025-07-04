package com.ehas.lotto.common.user.provider;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ehas.lotto.common.jwt.base.JwtTokenBase;
import com.ehas.lotto.common.jwt.service.JwtRedisSerivceImpt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class UserJwtTokenProvider extends JwtTokenBase{
    public UserJwtTokenProvider( JwtRedisSerivceImpt JwtRedisSerivceImpt) {
    	super(JwtRedisSerivceImpt);
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
    public String recreateAccessToken(String token){
			return super.recreateAccessToken(token);
    }
    @Override
    public String validdateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return super.validdateRefreshToken(request, response);
    }
    @Override
    public Boolean existsBlacklist(String token) throws Exception {
    	return super.existsBlacklist(token);
    }
    
    // AccessToken -> Authentication 생성 ( 인증된 사용자로 처리 )
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload();
        
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, Collections.emptyList());
    }
}