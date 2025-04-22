package com.ehas.center;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
public class CenterWebConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
        // CSRF 보호 기능 비활성화 (API 서버일 경우 주로 비활성화함)
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            // /eureka/**와 /actuator/** 경로는 모두 접근 허용
            .requestMatchers("/eureka/**").permitAll()
            .requestMatchers("/eureka/**", "/eureka/apps/**", "/eureka/peerreplication/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .anyRequest().authenticated()  // 나머지 요청은 인증 필요
        )
        // HTTP Basic 인증 활성화
        .httpBasic(Customizer.withDefaults());
        
    return httpSecurity.build();
    }
}
