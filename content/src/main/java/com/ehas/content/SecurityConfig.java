package com.ehas.content;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ehas.content.user.jwt.filter.UserJwtTokenAuthenticationFilter;
import com.ehas.content.user.jwt.provider.UserJwtTokenProvider;
import com.ehas.content.user.service.UserServiceImpt;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	 @Value("${message.security.allow-ip-list}")
	 private List<String> allowIpList;
	 
	 @Bean
	 public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
		 EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
		 config.setSecurePortEnabled(true);
		 return config;
	 }
	 
	    @Bean
	    @Order(1)
	    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http
	    													,UserJwtTokenProvider userJwtTokenProvider) throws Exception {

	        return http
	            .securityMatcher(new AntPathRequestMatcher("/users/**"))
	            .csrf(csrf -> csrf.disable())
	            .formLogin(login -> login.disable())
	            .httpBasic(basic -> basic.disable())
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .exceptionHandling(ex -> ex
	                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
	                .accessDeniedHandler((req, res, e) -> res.sendError(HttpStatus.FORBIDDEN.value()))
	            )
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers(HttpMethod.POST, "/users/*").permitAll()
	                .anyRequest().authenticated()
	            ).addFilterBefore(new UserJwtTokenAuthenticationFilter(userJwtTokenProvider), 
                        												UsernamePasswordAuthenticationFilter.class)
	            .build();
	    }

	    /*
	    @Bean
	    @Order(2)
	    public SecurityFilterChain contentSecurityFilterChain(HttpSecurity http,
	                                                          ContentJwtTokenProvider contentJwtTokenProvider) throws Exception {

	        return http
	            .securityMatcher(new AntPathRequestMatcher("/contents/**"))
	            .csrf(csrf -> csrf.disable())
	            .formLogin(login -> login.disable())
	            .httpBasic(basic -> basic.disable())
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .exceptionHandling(ex -> ex
	                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
	                .accessDeniedHandler((req, res, e) -> res.sendError(HttpStatus.FORBIDDEN.value()))
	            )
	            .addFilter(new ContentJwtTokenAuthenticationFilter(contentJwtTokenProvider))
	            .authorizeHttpRequests(auth -> auth
	                .anyRequest().authenticated()
	            )
	            .build();
	    }
	    */
	    
	    @Bean
	    @Order(3)
	    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
	    	String ipExpressions = allowIpList.stream()
	    		    .map(ip -> "hasIpAddress('" + ip.trim() + "')")
	    		    .collect(Collectors.joining(" or "));
	    	WebExpressionAuthorizationManager ipExcpressionManager = new WebExpressionAuthorizationManager(ipExpressions);
	    	
	        return http
	                .csrf(csrf -> csrf.disable())
	                .formLogin(form -> form.disable())
	                .httpBasic(basic -> basic.disable())
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .exceptionHandling(ex -> ex
	                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
	                    .accessDeniedHandler((req, res, e) -> res.sendError(HttpStatus.FORBIDDEN.value()))
	                )
	                .authorizeHttpRequests(auth -> auth
	                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                    .requestMatchers("/actuator/**").access(ipExcpressionManager)
	                    .requestMatchers("/address/**", "/cache/**", "/jwt/**").permitAll()
	                    .anyRequest().authenticated()
	                )
	                .build();
	    }

	    // 인증 관련 설정
	    @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public UserDetailsService userDetailsService(UserServiceImpt userServiceImpt) {
	        return username -> userServiceImpt.findUserDetailByUserId(username);
	        //userServiceImpt.findRoleByUserSeq(user.getSeq())
	        //user.setRoles(roles);
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(HttpSecurity http,
	                                                       UserDetailsService userDetailsService,
	                                                       BCryptPasswordEncoder passwordEncoder) throws Exception {
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        provider.setUserDetailsService(userDetailsService);
	        provider.setPasswordEncoder(passwordEncoder);

	        return http.getSharedObject(AuthenticationManagerBuilder.class)
	                .authenticationProvider(provider)
	                .build();
	    }
}
