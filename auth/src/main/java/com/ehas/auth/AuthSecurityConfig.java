package com.ehas.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@EnableWebFluxSecurity // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AuthSecurityConfig {
    
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) throws Exception {
    	 httpSecurity
    	 .csrf().disable()
         .authorizeExchange()
         .pathMatchers("/public/**").permitAll()
         .anyExchange().authenticated();
    	
     return httpSecurity.build();
    }
}
