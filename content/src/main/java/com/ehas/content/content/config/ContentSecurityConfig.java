package com.ehas.content.content.config;

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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ehas.content.content.jwt.filter.ContentJwtTokenAuthenticationFilter;
import com.ehas.content.content.jwt.provider.ContentJwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ContentSecurityConfig {
	@Value("${value.security.allow-ip-list}")
	private List<String> allowIpList;

	@Bean
	@Order(2)
	public SecurityFilterChain contentSecurityFilterChain(HttpSecurity http
															, ContentJwtTokenProvider JwtTokenProvider
															,@Qualifier("ContentAuthenticationProvider") DaoAuthenticationProvider authenticationProvider)throws Exception {
		AuthenticationManager authenticationManager = new ProviderManager(authenticationProvider);
		
		return http.securityMatcher(
					new AntPathRequestMatcher("/contents/**"))
				.authenticationManager(authenticationManager)
				.csrf(csrf -> csrf.disable())
				.formLogin(login -> login.disable()).httpBasic(basic -> basic.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
						.accessDeniedHandler((req, res, e) -> res.sendError(HttpStatus.FORBIDDEN.value())))
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/contents").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(new ContentJwtTokenAuthenticationFilter(JwtTokenProvider),
						UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
    @Bean("ContentAuthenticationProvider")
    public DaoAuthenticationProvider daoAuthenticationProvider(@Qualifier("ContentUserDetailService")UserDetailsService userDetailsService,
    															BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    
}
