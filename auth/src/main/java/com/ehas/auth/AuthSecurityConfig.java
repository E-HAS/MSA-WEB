package com.ehas.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.ehas.auth.User.service.UserServiceImpt;
import com.ehas.auth.jwt.filter.JwtTokenAuthenticationFilter;
import com.ehas.auth.jwt.service.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@EnableWebFluxSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@RequiredArgsConstructor
@Configuration
@EnableReactiveMethodSecurity
public class AuthSecurityConfig {
	//private final ApplicationContext applicationContext;
	
	@Value("${message.security.allow-ip-list}")
	private List<String> allowIpList;
	
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity
    										 ,ReactiveAuthenticationManager reactiveAuthenticationManager
    										 ,JwtTokenProvider jwtTokenProvider) throws Exception {
    	 httpSecurity
    	 .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                 .authenticationEntryPoint((exchange, ex) -> {
                	 
                 	System.out.println(">>>> UNAUTHORIZED before");
                     return Mono.fromRunnable(() -> {
            			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                     });
                 })
                 .accessDeniedHandler((exchange, denied) -> {
                  	System.out.println(">>>> FORBIDDEN before");
                     return Mono.fromRunnable(() -> {
                         exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                     });
                 }))
    	 
         .csrf(ServerHttpSecurity.CsrfSpec::disable)
         .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
         .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
         .authenticationManager(reactiveAuthenticationManager)
         .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
         .authorizeExchange(exchange -> exchange
                 .pathMatchers(HttpMethod.OPTIONS).permitAll()
                 .pathMatchers("/actuator/**").access(this::checkAllowIp)
                 .pathMatchers("/users").permitAll()
                 .pathMatchers("/users/*/token").permitAll()
                 .pathMatchers("/address/**").permitAll()
                 .pathMatchers("/contents/**").permitAll() //.access(this::checkAllowIp)//.hasAnyRole("CMS_ADMIN","CMS_USER")
                 .anyExchange().authenticated()
         )
         .addFilterAt(new JwtTokenAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.AUTHENTICATION);
         
     return httpSecurity.build();
    }
    
    private Mono<AuthorizationDecision> checkAllowIp(Mono<Authentication> authentication, AuthorizationContext context) {
        String accessIp = context.getExchange().getRequest().getRemoteAddress().getAddress().toString().replace("/", "");
        return Mono.just(new AuthorizationDecision(
                (allowIpList.contains(accessIp)) ? true : false));
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(UserServiceImpt userServiceImpt) {
        return username -> 
            	userServiceImpt.findByUserId(username)  // 사용자 정보 조회
                .flatMap(user -> userServiceImpt.findRoleByUserSeq(user.getSeq())
                				.collectList()
                				.flatMap(roles -> {
                                    String[] roleNames = roles.stream()
			                                            .map(role -> role.getRoleName()) 
			                                            .toArray(String[]::new); 
                                    
                					return Mono.just(org.springframework.security.core.userdetails.User
                									.withUsername(user.getUsername())
                									.password(user.getPassword())
                									.roles(roleNames)
                									.build()).log();
                				}));
    };

    
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
    
    @Bean
    public PermissionEvaluator myPermissionEvaluator() {
        return new PermissionEvaluator() {
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
            	System.out.println(">>>> hasPermission before");
                if(authentication.getAuthorities().stream()
                        .filter(grantedAuthority -> grantedAuthority.getAuthority().equals(targetDomainObject))
                        .count() > 0)
                    return true;
                return false;
            }

            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                return false;
            }
        };
    }
}
