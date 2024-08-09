package com.ehas.auth.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final ReactiveUserDetailsService reactiveUserDetailsService;
    private static final String AUTHORITIES_KEY = "permissions";

    @Value("${jwt.secretkey}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expirationTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        var secret = Base64.getEncoder().encodeToString(this.secret.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Claims claims = Jwts.claims().setSubject(username);
        if (authorities != null) {
            claims.put(AUTHORITIES_KEY
                    , authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        }

        Long expirationTimeLong = Long.parseLong(expirationTime);
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }
    
    public Mono<String> createTokenRx(Mono<Authentication> authentication) {
    	return authentication.flatMap(v->{ 
    	        String username = v.getName();
    	        Collection<? extends GrantedAuthority> authorities = v.getAuthorities();
    	        
    	        Claims claims = Jwts.claims().setSubject(username);
    	        if (authorities != null) {
    	            claims.put(AUTHORITIES_KEY
    	                    , authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
    	        }
    	        Long expirationTimeLong = Long.parseLong(expirationTime);
    	        final Date createdDate = new Date();
    	        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);
    	        
    	        return Mono.just(Jwts.builder()
		                .setClaims(claims)
		                .setSubject(username)
		                .setIssuedAt(createdDate)
		                .setExpiration(expirationDate)
		                .signWith(secretKey)
		                .compact());
    	});
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).getBody();

        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);
        System.out.println(">>>> getAuthoritiesClaim :"+authoritiesClaim);

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        System.out.println(">>>> getSubject :"+claims.getSubject());
        System.out.println(">>>> getAuthorities :"+authorities);
        
        //User principal = new User(claims.getSubject(), "", authorities);
        Mono<UserDetails> ud = reactiveUserDetailsService.findByUsername(claims.getSubject());
        System.out.println(">>>> UserDetails :"+ud);

        return new UsernamePasswordAuthenticationToken(ud, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder().setSigningKey(this.secretKey).build()
                    .parseClaimsJws(token);
            //  parseClaimsJws will check expiration date. No need do here.
            log.info("expiration date: {}", claims.getBody().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}