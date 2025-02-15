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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                                .antMatchers("/api/**").permitAll()
                                .antMatchers("/actuator/**").permitAll()
                                .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable());
     return httpSecurity.build();
    }
}

