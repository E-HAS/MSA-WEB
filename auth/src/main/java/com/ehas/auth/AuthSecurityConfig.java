package com.ehas.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import com.ehas.auth.User.service.UserServiceImpt;
import com.ehas.auth.jwt.filter.ContentJwtTokenAuthenticationFilter;
import com.ehas.auth.jwt.filter.UserJwtTokenAuthenticationFilter;
import com.ehas.auth.jwt.service.ContentJwtTokenProvider;
import com.ehas.auth.jwt.service.UserJwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity
public class AuthSecurityConfig {
	
	@Value("${message.security.allow-ip-list}")
	private List<String> allowIpList;
	
    private Mono<AuthorizationDecision> checkAllowIp(Mono<Authentication> authentication, AuthorizationContext context) {
        String accessIp = context.getExchange().getRequest().getRemoteAddress().getAddress().toString().replace("/", "");
        return Mono.just(new AuthorizationDecision(
                (allowIpList.contains(accessIp)) ? true : false));
    }
    
    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        config.setSecurePortEnabled(true); 
        return config;
    }
	
    @Bean
    @Order(1)
    public SecurityWebFilterChain userSecurityFilterChain(ServerHttpSecurity httpSecurity
														  ,ReactiveAuthenticationManager reactiveAuthenticationManager
														  ,UserJwtTokenProvider userJwtTokenProvider) {
        return httpSecurity
           	 .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                     .authenticationEntryPoint((exchange, ex) -> {
                    	 
                     	System.out.println(">>>> UNAUTHORIZED USER");
                         return Mono.fromRunnable(() -> {
                			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                         });
                     })
                     .accessDeniedHandler((exchange, denied) -> {
                      	System.out.println(">>>> FORBIDDEN USER");
                         return Mono.fromRunnable(() -> {
                             exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                         });
                     }))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
	            .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/users/**"))
	            .authorizeExchange(exchanges -> exchanges
	            		.pathMatchers(HttpMethod.POST, "/users/*").permitAll()
	                    .anyExchange().authenticated()
	            )
	            .addFilterAt(new UserJwtTokenAuthenticationFilter(userJwtTokenProvider), SecurityWebFiltersOrder.AUTHENTICATION)
	            .build();
    }
    
    @Bean
    @Order(2)
    public SecurityWebFilterChain contentSecurityFilterChain(ServerHttpSecurity httpSecurity
														  ,ReactiveAuthenticationManager reactiveAuthenticationManager
														  ,ContentJwtTokenProvider contentJwtTokenProvider) {
        return httpSecurity
           	 .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                     .authenticationEntryPoint((exchange, ex) -> {
                    	 
                     	System.out.println(">>>> UNAUTHORIZED CONTENT");
                         return Mono.fromRunnable(() -> {
                			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                         });
                     })
                     .accessDeniedHandler((exchange, denied) -> {
                      	System.out.println(">>>> FORBIDDEN CONTENT");
                         return Mono.fromRunnable(() -> {
                             exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                         });
                     }))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
	            .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/contents/**"))
	            .authorizeExchange(exchanges -> exchanges
	                    .anyExchange().authenticated()
	            )
	            .addFilterAt(new ContentJwtTokenAuthenticationFilter(contentJwtTokenProvider), SecurityWebFiltersOrder.AUTHENTICATION)
	            .build();
    }
    
    
    
    @Bean
    @Order(3)
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity httpSecurity
    										 ,ReactiveAuthenticationManager reactiveAuthenticationManager) throws Exception {
    	 httpSecurity
    	 .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                 .authenticationEntryPoint((exchange, ex) -> {
                	 
                 	System.out.println(">>>> UNAUTHORIZED DEFAULT");
                     return Mono.fromRunnable(() -> {
            			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                     });
                 })
                 .accessDeniedHandler((exchange, denied) -> {
                  	System.out.println(">>>> FORBIDDEN DEFAULT");
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
                 .pathMatchers("/address/**").permitAll()
                 .pathMatchers("/cache/**").permitAll()
                 .pathMatchers("/jwt/**").permitAll()
                 .anyExchange().authenticated()
         );
         
     return httpSecurity.build();
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
                                .map(roles -> {
                                    user.setRoles(roles); // roles 주입
                                    return user; // ✅ Mono<UserEntity> -> Mono<UserDetails>
                                })
                          );
                                /*
                				.flatMap(roles -> {
                					
                                    String[] roleNames = roles.stream()
			                                            .map(role -> role.getRoleName()) 
			                                            .toArray(String[]::new); 
                                    
                					return Mono.just(org.springframework.security.core.userdetails.User
                									.withUsername(user.getUsername())
                									.
                									.password(user.getPassword())
                									.roles(roleNames)
                									.build()).log();
                				}));
                				*/
    };

    
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
}
