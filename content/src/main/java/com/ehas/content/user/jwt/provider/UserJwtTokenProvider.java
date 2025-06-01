package com.ehas.content.user.jwt.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.ehas.content.user.jwt.base.JwtTokenBase;

import jakarta.servlet.http.HttpServletRequest;
@Slf4j
@Component
public class UserJwtTokenProvider extends JwtTokenBase{
    public UserJwtTokenProvider(UserDetailsService userDetailsService) {
        super(userDetailsService);
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
}