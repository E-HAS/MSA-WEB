package com.ehas.auth;

import java.io.Serializable;
import java.util.ArrayList;
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
//@EnableWebFlux
@EnableWebFluxSecurity // 필터 체인 관리 시작 어노테이션
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
    	 //DefaultMethodSecurityExpressionHandler defaultWebSecurityExpressionHandler = this.applicationContext.getBean(DefaultMethodSecurityExpressionHandler.class);
    	 //defaultWebSecurityExpressionHandler.setPermissionEvaluator(myPermissionEvaluator());
    	 //DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    	 //expressionHandler.setPermissionEvaluator(myPermissionEvaluator());
        
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
    	 
         .csrf().disable()
         .formLogin().disable()
         .httpBasic().disable() // request header <- Authorization: Basic dXNlcjoxMTEx 값은 방식으로 formLogin 같은걸 해제
         .authenticationManager(reactiveAuthenticationManager)
         .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // stateless 설정
         .authorizeExchange(exchange -> exchange
                 .pathMatchers(HttpMethod.OPTIONS).permitAll()
                 .pathMatchers("/auth/**").access(this::checkAllowIp)
                 .pathMatchers("/public/**").permitAll()
                 .pathMatchers("/cms/**").hasRole("ADMIN")
                 .anyExchange().authenticated()
         )
         .addFilterAt(new JwtTokenAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.AUTHENTICATION);
         
     return httpSecurity.build();
    }
    
    private Mono<AuthorizationDecision> checkAllowIp(Mono<Authentication> authentication, AuthorizationContext context) {
        String accessIp = context.getExchange().getRequest().getRemoteAddress().getAddress().toString().replace("/", "");
        return Mono.just(new AuthorizationDecision(
                (allowIpList.contains(accessIp)) ? true : false));
        /*
        return authentication.map((v) -> new AuthorizationDecision(v.isAuthenticated()))
                .defaultIfEmpty(new AuthorizationDecision(
                        (allowIpList.contains(accessIp)) ? true : false
                )).log();
        */
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(UserServiceImpt userServiceImpt) {
        return username -> {
        	System.out.println(">>>> ReactiveUserDetailsService ing Username :"+username);
        	
        	ArrayList<String> roleList = new ArrayList<String>();
        	userServiceImpt.findByUserRoleRx(username).map(v->v.getUserRole())
        													.collectList().log()
        													.subscribe(roleList::addAll);      	
        	System.out.println(">>>> ReactiveUserDetailsService ing roleList :"+roleList);
        	
            return 		userServiceImpt.findByRxUserId(username)
       			 		 .map(user -> org.springframework.security.core.userdetails.User
                         .withUsername(user.getUsername())
                         .password(user.getPassword())
                         .roles(roleList.toArray(String[]::new)
                        		 //user.getRoles().stream().map(v->v.getUserRole()).toArray(String[]::new)
                        		 )
                         .build());
        };
    }
    
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
