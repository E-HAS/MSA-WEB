package com.ehas.content.user.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ehas.content.content.jwt.provider.ContentJwtTokenProvider;
import com.ehas.content.user.jwt.filter.UserJwtTokenAuthenticationFilter;
import com.ehas.content.user.jwt.provider.UserJwtTokenProvider;
import com.ehas.content.user.principal.service.UserPrincipalDetailsService;
import com.ehas.content.user.service.UserServiceImpt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class UserSecurityConfig {
	@Value("${message.security.allow-ip-list}")
	private List<String> allowIpList;

	@Bean
	@Order(1)
	public SecurityFilterChain userSecurityFilterChain(HttpSecurity http
														,UserJwtTokenProvider userJwtTokenProvider
														,@Qualifier("UserAuthenticationProvider") DaoAuthenticationProvider authenticationProvider) throws Exception {
	    
		AuthenticationManager authenticationManager = new ProviderManager(authenticationProvider);
		
		return http.securityMatcher(
					new AntPathRequestMatcher("/users/**"))
				.authenticationManager(authenticationManager)
				.csrf(csrf -> csrf.disable())
				.formLogin(login -> login.disable())
				.httpBasic(basic -> basic.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
						.accessDeniedHandler((req, res, e) -> res.sendError(HttpStatus.FORBIDDEN.value())))
				.authorizeHttpRequests(auth -> 
						auth.requestMatchers(HttpMethod.POST, "/users/*").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(new UserJwtTokenAuthenticationFilter(userJwtTokenProvider),
						UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	

    @Bean("UserAuthenticationProvider")
    public DaoAuthenticationProvider daoAuthenticationProvider(@Qualifier("UserUserDetailService")UserDetailsService userDetailsService,
    															BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
	
}
