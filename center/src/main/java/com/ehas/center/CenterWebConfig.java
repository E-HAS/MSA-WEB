package com.ehas.center;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
public class CenterWebConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	 httpSecurity
    	 .csrf()
         .disable()
         .authorizeRequests()
         .antMatchers("/eureka/**").permitAll()
         .antMatchers("/apps/**").permitAll()
         .anyRequest().authenticated()
         .and()
         .httpBasic();
    	
     return httpSecurity.build();
    }
}

