package com.ehas.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class InfraSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
        // CSRF 보호 기능 비활성화 (API 서버일 경우 주로 비활성화함)
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/eureka/**"
            				, "/eureka/apps/**"
            				, "/eureka/peerreplication/**").authenticated()
            .requestMatchers("/api/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/stomp/**").permitAll()
            .anyRequest().permitAll()  // 나머지 요청은 인증 필요
        ).httpBasic(Customizer.withDefaults());
        
    return httpSecurity.build();
    }
}
