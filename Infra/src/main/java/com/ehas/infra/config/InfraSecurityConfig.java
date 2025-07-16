package com.ehas.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class InfraSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
        // CSRF 보호 기능 비활성화 (API 서버일 경우 주로 비활성화함)
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {}) // CORS 활성화 부분 추가
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
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        //configuration.addAllowedOrigin("http://192.168.1.102");
        //configuration.addAllowedOrigin("https://192.168.1.102");  // 허용할 출처
        configuration.addAllowedMethod("*");                     // 모든 HTTP 메서드 허용
        configuration.addAllowedHeader("*");                     // 모든 헤더 허용
        configuration.setAllowCredentials(true);                 // 쿠키/자격증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
