package com.ehas.content.common.jwt.base;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import com.ehas.content.common.jwt.dto.JwtToken;
import com.ehas.content.common.jwt.service.JwtRedisSerivceImpt;
import com.ehas.content.user.principal.entity.UserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenBase {
    public static final String HEADER_PREFIX = "Bearer ";
    protected static final String PERMISSIONS_KEY = "permissions";
    protected static final String TOKEN_ID_KEY = "tokenId";

    @Value("${jwt.secretkey}")
    protected String secret;

    @Value("${jwt.access-Token-expiration}")
    protected long accessTokenexpirationTime;

    @Value("${jwt.refresh-token-expiration}")
    protected long refreshTokenExpirationTime;

    protected SecretKey secretKey;

    protected  final JwtRedisSerivceImpt JwtRedisSerivceImpt;
    protected  JwtTokenBase(JwtRedisSerivceImpt JwtRedisSerivceImpt) {
       this.JwtRedisSerivceImpt = JwtRedisSerivceImpt;
   }
   
    @PostConstruct
    public void init() {
        //var secret = Base64.getEncoder().encodeToString(this.secret.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(HEADER_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new Exception("Invalid JWT signature: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new Exception("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new Exception("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new Exception("JWT claims string is empty: " + e.getMessage());
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new Exception("Invalid JWT token: " + e.getMessage());
        }
    }

    public Claims getPayloadToken(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            return e.getClaims();
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return null;
    }

    public String createTokenByPayload(Claims payload, Date now, Date expiry) {
        return Jwts.builder()
                .claims(payload)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public long getAccessTokenexpirationTime() {
        return accessTokenexpirationTime;
    }
    
    // AccessToken 재생성
    public String recreateAccessToken(String token){
	    	Date now = new Date();
	        Date expiryDate = new Date(now.getTime() + this.getAccessTokenexpirationTime());
			Claims payload = this.getPayloadToken(token);
			if(payload == null) {
				new Exception("Failed Recreate AccessToken");
			}
			return this.createTokenByPayload(payload, now, expiryDate);
    }
    
    // Authentication -> AccessToken 생성
    public JwtToken createAccessToken(Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        
        /*
        String permissions = authentication.getAuthorities().stream()	//Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        */
        
        String accessTokenId = UUID.randomUUID().toString();
        
        Claims claims = Jwts.claims()
        		.subject(userDetail.getId())
        		//.add(PERMISSIONS_KEY, permissions)
        		.add(TOKEN_ID_KEY, accessTokenId)
        		.add("name", userDetail.getName())
        		.add("address", userDetail.getAddressSeq())
        		.build();
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenexpirationTime);
        
        String accessToken = Jwts.builder()
				                .claims(claims)
				                .issuedAt(now)
				                .expiration(expiryDate)
				                .signWith(secretKey)
				                .compact();
        
        return JwtToken.builder()
        				.prefix(HEADER_PREFIX)
        				.accessTokenId(accessTokenId)
        				.accessToken(accessToken)
        				.build();
    }
    
    // Authentication -> RefreshToken 생성
    public JwtToken createRefreshToken(Authentication authentication) {
    	String refresTokenId = UUID.randomUUID().toString();
    	String username = authentication.getName();
        
        Claims claims = Jwts.claims()
        					.subject(username)
        					.add(TOKEN_ID_KEY, refresTokenId)
        					.build();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationTime);
        
        String refreshToken = Jwts.builder()
				                .claims(claims)
				                .issuedAt(now)
				                .expiration(expiryDate)
				                .signWith(secretKey)
				                .compact();
        
        return JwtToken.builder()
        				.prefix(HEADER_PREFIX)
						.refreshTokenId(refresTokenId)
						.refreshToken(refreshToken)
						.build();
    }
    
    
    // RefreshToken 유효성검사
    public String validdateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // 1. 쿠키에서 refreshToken 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
        	throw new Exception("Refresh token not found in cookies.");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
        	throw new Exception("Refresh token not found in cookies.");
        }

        // 2. Redis에 존재하는지 확인
        if (!JwtRedisSerivceImpt.existsRefreshToken(refreshToken)) {
        	throw new Exception(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        // 3. 토큰 유효성 검사
        try {
            if (!this.validateToken(refreshToken)) {
            	throw new Exception("Invalid refresh token.");
            }
        } catch (Exception e) {
        	throw new Exception(e.getMessage());
        }
        // 4. 새로운 액세스 토큰 재발급
        String accessTokenInHeader = this.resolveAccessToken(request);
        String recreatedAccessToken = this.recreateAccessToken(accessTokenInHeader);

        // 5. 응답 헤더에 액세스 토큰 추가
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + recreatedAccessToken);
        
        return recreatedAccessToken;
    }

    // AccessToken 블랙리스트 존재여부
    public Boolean existsBlacklist(String token) throws Exception {
    	try {
    		if(!JwtRedisSerivceImpt.existsBlacklistToken(token)) {
    			return true;
    		}else {
    			throw new Exception("Invalid JWT token");
    		}
    	}catch (Exception e) {
    		throw new Exception("Invalid JWT token");
    	}
    }
}
