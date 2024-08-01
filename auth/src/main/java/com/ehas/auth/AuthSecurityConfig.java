package com.ehas.auth;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.ehas.auth.dto.CustomUserDetails;
import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.jwt.JwtTokenAuthenticationFilter;
import com.ehas.auth.jwt.JwtTokenProvider;
import com.ehas.auth.service.CustomReactiveAuthenticationManager;

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
	
	//private final CustomReactiveAuthenticationManager customReactiveAuthenticationManager;
	
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity
    										 ,CustomReactiveAuthenticationManager customReactiveAuthenticationManager
    										 ,JwtTokenProvider jwtTokenProvider) throws Exception {
    	//DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    	//expressionHandler.setPermissionEvaluator(myPermissionEvaluator());
        
    	 httpSecurity
    	 .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                 .authenticationEntryPoint((exchange, ex) -> {
                 	System.out.println(">>>> UNAUTHORIZED before");
                	log.debug(">>>> UNAUTHORIZED before");
                     return Mono.fromRunnable(() -> {
                         exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                     });
                 })
                 .accessDeniedHandler((exchange, denied) -> {
                  	System.out.println(">>>> FORBIDDEN before");
                 	log.debug(">>>> FORBIDDEN before");
                     return Mono.fromRunnable(() -> {
                         exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                     });
                 }))
    	 
         .csrf().disable()
         .formLogin().disable()
         .httpBasic().disable() // request header <- Authorization: Basic dXNlcjoxMTEx 값은 방식으로 formLogin 같은걸 해제
         .authenticationManager(customReactiveAuthenticationManager)
         .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // stateless 설정
         .authorizeExchange(exchange -> exchange
                 .pathMatchers(HttpMethod.OPTIONS).permitAll()
                 .pathMatchers("/public/**").permitAll()
                 .pathMatchers("/cms/**").hasRole("USER")
                 .anyExchange().authenticated()
         )
         .addFilterAt(new JwtTokenAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.HTTP_BASIC);
         
     return httpSecurity.build();
    }
    /*
    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
    	return new ReactiveUserDetailsService() {
    		@Override
    		public Mono<UserDetails> findByUsername(String username) {
    	    	CustomUserDetails cud = new CustomUserDetails();
    	    	cud.setUsername(username);
    	    	cud.setPassword("");
    	    	System.out.println(">>>> CustomUserDetail ing :"+cud);
    	    	
    	        return Mono.just(cud);
    		}
    	};
    	
    	// return username ->  rxUserRepository.findByRxUserId(username).orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND"));
    }
    */
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
