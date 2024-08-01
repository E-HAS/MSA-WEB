package com.ehas.auth.service;

import org.springframework.stereotype.Service;

import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.reactive.ReactiveUserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final ReactiveUserRepository UserRepoRx;
	
	public Mono<UserEntity> findByIdRx(String uid){
		return UserRepoRx.findByIdRx(uid);
	}
	
	public UserEntity findByRxUserId(String id){
		return UserRepoRx.findByUserId(id);
	}
}
