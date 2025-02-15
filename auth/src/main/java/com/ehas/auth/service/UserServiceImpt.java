package com.ehas.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.auth.dto.UserDto;
import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.entity.UserRoleEntity;
import com.ehas.auth.entity.UserRoleKey;
import com.ehas.auth.reactive.ReactiveUserEntityRepository;
import com.ehas.auth.reactive.ReactiveUserRoleEntityRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final ReactiveUserEntityRepository UserRepoRx;
	private final ReactiveUserRoleEntityRepository UserRoleRepoRx;
	
	//@Transactional(rollbackFor = { Exception.class })  
	public Mono<Boolean> saveByUserEntity(UserDto user){
		UserRepoRx.save(UserEntity.builder()
				                  .userType(user.getUserType())
				                  .userId(user.getUserId())
				                  .userPassword(user.getUserPassword())
				                  .nickName(user.getUserName())
				                  .userState("Y")
				                  .build()
		)
		.flatMap(userEntity -> {
			UserRoleEntity userRole = UserRoleEntity.builder()
		            .userType(userEntity.getUserType())
		            .userUid(userEntity.getUid())
		            .userRole("USER")
		            .build();
		    return UserRoleRepoRx.save(userRole);
		}).subscribe(
				result -> System.out.println("작업 성공: " + result), 
			    error -> System.out.println("작업 실패: " + error.getMessage())); 
		return Mono.just(true);
	}
	
	
	public Mono<UserEntity> findByRxUid(String uid){
		return UserRepoRx.findByRxUid(uid);
	}
	
	public Mono<UserEntity> findByRxUserId(String id){
		return UserRepoRx.findByRxUserId(id);
	}
	
	public Flux<UserRoleEntity> findUserRoleByRxUserId(String id){
		return UserRoleRepoRx.findUserRoleByRxUserId(id);
	}
	
	public Flux<UserRoleEntity> findUserRoleByRxUid(Integer uid){
		return UserRoleRepoRx.findUserRoleByRxUid(uid);
	}
}
