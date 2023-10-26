package com.xekidd.stomp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    	 				.csrf().disable() // token를 사용하는 방식으로 csrf를 Disable
    	 				
    	 				/*
    	 				.exceptionHandling()
    	 				.authenticationEntryPoint(null)
    	 				.accessDeniedHandler(null)
    	 				*/
    	 				
    	 				.sessionManagement()
    	 				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증으로 세션 사용하지 않기
    	 
    	 				.and()
    	 				.authorizeHttpRequests()
    	 				.antMatchers("/Meet/**"
    	 							,"/Stomp/**"
    	 							,"/css/**"
    	 							, "/img/**"
    	 							, "/js/**").permitAll()
    	 				.anyRequest().authenticated();
    	 				
    	 				/*
    	 				.and()
    	 				.addFilterBefore(new JwtAuthenticationFilter(jwtServiceImpt)
    	 						, UsernamePasswordAuthenticationFilter.class);
    	 				*/
    	
     return httpSecurity.build();
    }
}
