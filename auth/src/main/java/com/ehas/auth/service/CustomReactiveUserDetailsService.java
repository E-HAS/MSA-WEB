package com.ehas.auth.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ehas.auth.dto.CustomUserDetails;
import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.reactive.ReactiveUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/*
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService{

	private final ReactiveUserRepository rxUserRepository;
	
	@Override
	public Mono<UserDetails> findByUsername(String username) {
    	System.out.println(">>>> userDetail before :"+username);
    	
		Mono<UserEntity> ue = rxUserRepository.findByRxUserId(username);
    	System.out.println(">>>> userDetail ing :"+username);
    	
    	CustomUserDetails cud = new CustomUserDetails();
    	cud.setUsername(username);
    	cud.setPassword("$2a$10$TuMbL7TO.jaj.b8R3Y50B.YtiEvGbjoC2EqHqzeqipTc4kLoTZT32");
    	System.out.println(">>>> CustomUserDetail ing :"+cud);
    	
        return Mono.just(cud);
	}

}
*/