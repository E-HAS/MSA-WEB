package com.ehas.auth.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ehas.auth.User.entity.UserEntity;
import com.ehas.auth.jwt.dto.JwtUserDto;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserJwtTokenProvider {
	public static final String HEADER_PREFIX = "Bearer ";
	
	private final ReactiveUserDetailsService reactiveUserDetailsService;
    private static final String PERMISSIONS_KEY = "permissions";

    @Value("${jwt.secretkey}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expirationTime;
    
    private final long refreshTokenExpirationMillis = 7 * 24 * 60 * 60 * 1000L; // 7일

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        //var secret = Base64.getEncoder().encodeToString(this.secret.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        //Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        Claims claims = Jwts.claims()
        		.subject(userEntity.getId())
        		.add(PERMISSIONS_KEY, permissions)
        		.add("id", userEntity.getId())
        		.add("name", userEntity.getName())
        		.add("address", userEntity.getAddressSeq())
        		.build();
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Long.parseLong(expirationTime));
        
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
    
    public String createRefreshToken(Authentication authentication) {
    	String username = authentication.getName();
        
    	String tokenId = UUID.randomUUID().toString();
        Claims claims = Jwts.claims()
        					.subject(username)
        					.add("tokenId", tokenId)
        					.build();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMillis);

        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }
    
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(this.secretKey).build()
                .parseSignedClaims(token).getPayload();

        Object authoritiesClaim = claims.get(PERMISSIONS_KEY);
        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());
        
        Mono<UserDetails> userDetail = reactiveUserDetailsService.findByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetail, token, authorities);
    }
    
    public String recreateToken(String token){
    	
    	Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Long.parseLong(expirationTime));
        
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(this.secretKey).build()
                    .parseSignedClaims(token).getPayload();
            return Jwts.builder()
                    .claims(claims)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(secretKey)
                    .compact();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이지만 재생성
            return Jwts.builder()
                    .claims(e.getClaims())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(secretKey)
                    .compact();
        } catch (JwtException e) {
            // 유효하지 않은 토큰
        	throw new RuntimeException("Invalid JWT token: "+e.getMessage());
        }
    }

    public boolean validateToken(String token) throws Exception {
        try {
            Jws<Claims> claims = Jwts.parser()
            		.verifyWith(secretKey).build()
            		.parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new Exception("Invalid JWT signature: "+e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new Exception("JWT token is expired: "+e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new Exception("JWT token is unsupported: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new Exception("JWT claims string is empty: "+e.getMessage());
        }catch (JwtException e){
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new Exception("Invalid JWT token: "+e.getMessage());
        }
    }
    
    public String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}