package com.ehas.auth.service;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager{

	//private final CustomReactiveUserDetailsService cReactiveUserDetailsService;
    //private final BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
     	System.out.println(">>>> authenticate before :"+authentication);

		/*
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(cReactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        System.out.println(">>>> authenticate authenticationManager: "+authenticationManager.toString());
        System.out.println(">>>> authenticate password Encoder :"+passwordEncoder.toString());
        */
		return Mono.just(null);
	}

}
