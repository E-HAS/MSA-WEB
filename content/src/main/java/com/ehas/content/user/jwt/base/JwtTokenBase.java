package com.ehas.content.user.jwt.base;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.jwt.dto.JwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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

    protected final UserDetailsService userDetailsService;
    
    protected JwtTokenBase(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
    
    // AccessToken -> Authentication 생성 ( 인증된 사용자로 처리 )
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload();
        
        /*
        Object authoritiesClaim = claims.get(PERMISSIONS_KEY);
        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());
        */
        
        UserDetails userDetail = userDetailsService.loadUserByUsername(claims.getSubject());
        Collection<? extends GrantedAuthority> authorities = userDetail.getAuthorities();
        
        return new UsernamePasswordAuthenticationToken(userDetail, token, authorities);
    }
    
    // Authentication -> AccessToken 생성
    public JwtToken createAccessToken(Authentication authentication) {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        
        /*
        String permissions = authentication.getAuthorities().stream()	//Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        */
        
        String accessTokenId = UUID.randomUUID().toString();
        
        Claims claims = Jwts.claims()
        		.subject(userEntity.getId())
        		//.add(PERMISSIONS_KEY, permissions)
        		.add(TOKEN_ID_KEY, accessTokenId)
        		.add("name", userEntity.getName())
        		.add("address", userEntity.getAddressSeq())
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
}
