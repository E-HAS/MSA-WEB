package com.ehas.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.ehas.auth.dto.CustomUserDetails;
import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.entity.UserRole;
import com.ehas.auth.jwt.JwtTokenAuthenticationFilter;
import com.ehas.auth.jwt.JwtTokenProvider;
import com.ehas.auth.reactive.ReactiveUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
//@EnableWebFlux
@EnableWebFluxSecurity // 필터 체인 관리 시작 어노테이션
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@Configuration
@EnableReactiveMethodSecurity
public class AuthSecurityConfig {
	private final ApplicationContext applicationContext;
	
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity
    										 ,JwtTokenProvider jwtTokenProvider
    										 ,ReactiveAuthenticationManager reactiveAuthenticationManager) throws Exception {
    	DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    	expressionHandler.setPermissionEvaluator(myPermissionEvaluator());
        
    	 httpSecurity
    	 .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                 .authenticationEntryPoint((exchange, ex) -> {
                     return Mono.fromRunnable(() -> {
                         exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                     });
                 })
                 .accessDeniedHandler((exchange, denied) -> {
                     return Mono.fromRunnable(() -> {
                         exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                     });
                 }))
    	 
         .csrf().disable()
         .formLogin().disable()
         .httpBasic().disable()
         .authenticationManager(reactiveAuthenticationManager)
         .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
         .authorizeExchange(exchange -> exchange
                 .pathMatchers(HttpMethod.OPTIONS).permitAll()
                 .pathMatchers("/public/**").permitAll()
                 .pathMatchers("/cms/**").hasRole("ROLE_ADMIN")
                 .anyExchange().authenticated()
         )
         .addFilterAt(new JwtTokenAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.HTTP_BASIC);
         
     return httpSecurity.build();
    }
    
    @Bean
    public ReactiveUserDetailsService  userDetailsService(ReactiveUserRepository rxUserRepository) {
    	System.out.println(">>>> userDetail before");
    	log.debug(">>>> userDetail before");
    	
    	return username -> {
    		User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
    		UserEntity ue = rxUserRepository.findByRxUserId(username);
        	System.out.println(">>>> userDetail ing :"+username);
        	log.debug(">>>> userDetail ing :"+username);
        	CustomUserDetails cud = new CustomUserDetails();
        	
            return Mono.just(cud);
    	};
    }
    
    /*
    @Bean
    public MapReactiveUserDetailsService  userDetailsService(ReactiveUserRepository rxUserRepository) {
    	System.out.println(">>>> userDetail before");
    	log.debug(">>>> userDetail before");
    	User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        UserDetails rob = userBuilder.username("rob")
                .password("rob")
                .roles("USER")
                .build();
        UserDetails admin = userBuilder.username("admin")
                .password("admin")
                .roles("USER","ADMIN")
                .build();
    	//UserEntity rxUser = rxUserRepository.findByRxUserId("testid");
        return new MapReactiveUserDetailsService(rob,admin);

    }
    */
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
    																	BCryptPasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        System.out.println(">>>> password Encoder "+passwordEncoder.toString());
        log.debug(">>>> password Encoder "+passwordEncoder.toString());
        return authenticationManager;
    }
    
    
    
    @Bean
    public PermissionEvaluator myPermissionEvaluator() {
        return new PermissionEvaluator() {
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
            	System.out.println(">>>> hasPermission before");
            	log.debug(">>>> hasPermission before");
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
