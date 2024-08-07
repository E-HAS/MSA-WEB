package com.ehas.auth.service;

import org.springframework.stereotype.Service;

import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.entity.UserRole;
import com.ehas.auth.reactive.ReactiveUserRepository;
import com.ehas.auth.reactive.ReactiveUserRoleRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final ReactiveUserRepository UserRepoRx;
	private final ReactiveUserRoleRepository UserRoleRepoRx;
	
	public Mono<UserEntity> findByIdRx(String uid){
		return UserRepoRx.findByIdRx(uid);
	}
	
	public Mono<UserEntity> findByRxUserId(String id){
		return UserRepoRx.findByRxUserId(id);
	}
	
	public Flux<UserRole> findByUserRoleRx(String id){
		return UserRoleRepoRx.findByUserIdRx(id);
	}
}
