package com.ehas.auth.service;

import org.springframework.stereotype.Service;

import com.ehas.auth.entity.User;
import com.ehas.auth.reactive.ReactiveUserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final ReactiveUserRepository UserRepoRx;
	
	public Mono<User> findByIdRx(String uid){
		return UserRepoRx.findByIdRx(uid);
	}
}
