package com.xekidd.stomp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class WebSecurityConfig {
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
        .csrf(csrf -> csrf.disable()) // 토큰 기반 인증을 사용할 때 CSRF 비활성화
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않도록 설정
        )
        .authorizeHttpRequests(authz -> authz
            .antMatchers("/meet/**").permitAll() // /Meet/** 경로에 대해 모든 요청 허용
            .antMatchers("/Stomp/**").permitAll() // /Stomp/** 경로에 대해 모든 요청 허용
            .antMatchers("/css/**").permitAll() // /css/** 경로에 대해 모든 요청 허용
            .antMatchers("/img/**").permitAll() // /img/** 경로에 대해 모든 요청 허용
            .antMatchers("/js/**").permitAll() // /js/** 경로에 대해 모든 요청 허용
            .anyRequest().permitAll() // 나머지 모든 요청에 대해 허용
        );
        // JWT 필터를 사용한다면 아래와 같이 필터를 추가할 수 있습니다:
        // .addFilterBefore(new JwtAuthenticationFilter(jwtServiceImpt), UsernamePasswordAuthenticationFilter.class);	
     return httpSecurity.build();
    }
}
