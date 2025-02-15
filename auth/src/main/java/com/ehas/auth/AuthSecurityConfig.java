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

import com.ehas.auth.jwt.JwtTokenAuthenticationFilter;
import com.ehas.auth.jwt.JwtTokenProvider;
import com.ehas.auth.service.UserServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@EnableWebFluxSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@Configuration
@EnableReactiveMethodSecurity
public class AuthSecurityConfig {
	private final ApplicationContext applicationContext;
	
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
                 .pathMatchers("/auth/**").access(this::checkAllowIp)
                 .pathMatchers("/actuator/**").access(this::checkAllowIp)
                 .pathMatchers("/public/**").permitAll()
                 .pathMatchers("/cms/**").hasAnyRole("CMS_ADMIN","CMS_USER")
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
            userServiceImpt.findByRxUserId(username)  // 사용자 정보 조회
                .flatMap(user -> 
                    userServiceImpt.findUserRoleByRxUid(user.getUid())  // 역할 정보 조회
                        .collectList()  // Flux<UserRoleEntity> -> List<UserRoleEntity>로 변환
                        .map(userRoles -> 
                            org.springframework.security.core.userdetails.User
                                .withUsername(user.getUsername())
                                .password(user.getPassword())
                                .roles(userRoles.stream()
                                    .map(v -> v.getUserRole())  // UserRoleEntity에서 userRole을 추출
                                    .toArray(String[]::new))  // String[]로 변환
                                .build()
                        )
                );
    };

    
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
    	System.out.println(">>>> ReactiveAuthenticationManager before");
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
