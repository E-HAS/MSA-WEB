package com.ehas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
public class ConfigWebConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
        // CSRF 보호 기능 비활성화 (API 서버일 경우 주로 비활성화함)
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .anyRequest().permitAll()
        )
        // HTTP Basic 인증 비활성화
        .httpBasic(httpBasic -> httpBasic.disable());
    	return httpSecurity.build();
    }
}

