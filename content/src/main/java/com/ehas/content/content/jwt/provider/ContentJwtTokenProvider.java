package com.ehas.content.content.jwt.provider;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.ehas.content.common.jwt.base.JwtTokenBase;
import com.ehas.content.common.jwt.service.JwtRedisSerivceImpt;
import com.ehas.content.user.redis.service.UserRedisSerivceImpt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class ContentJwtTokenProvider extends JwtTokenBase{
	private final UserDetailsService userDetailsService;
	private final UserRedisSerivceImpt userRedisSerivceImpt;
	
    public ContentJwtTokenProvider(@Qualifier("ContentUserDetailService")UserDetailsService userDetailsService
    								, JwtRedisSerivceImpt JwtRedisSerivceImpt
    								, UserRedisSerivceImpt userRedisSerivceImpt) {
    	super(JwtRedisSerivceImpt);
    	this.userDetailsService = userDetailsService;
    	this.userRedisSerivceImpt = userRedisSerivceImpt;
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
        
        UserDetails userDetail = userDetailsService.loadUserByUsername(claims.getSubject());
        Collection<? extends GrantedAuthority> authorities = userDetail.getAuthorities();
        
        return new UsernamePasswordAuthenticationToken(userDetail, token, authorities);
    }
}