package com.ehas.content.common.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class SecurityConfig {
	@Value("${message.security.allow-ip-list}")
	private List<String> allowIpList;

	@Bean
	@Order(3)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		String ipExpressions = allowIpList.stream().map(ip -> "hasIpAddress('" + ip.trim() + "')")
				.collect(Collectors.joining(" or "));
		WebExpressionAuthorizationManager ipExcpressionManager = new WebExpressionAuthorizationManager(ipExpressions);

		return http.csrf(csrf -> csrf.disable()).formLogin(form -> form.disable()).httpBasic(basic -> basic.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
						.accessDeniedHandler((req, res, e) -> res.sendError(HttpStatus.FORBIDDEN.value())))
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/actuator/**").access(ipExcpressionManager)
						.requestMatchers("/address/**", "/cache/**", "/jwt/**").permitAll().anyRequest()
						.authenticated())
				.build();
	}
}
