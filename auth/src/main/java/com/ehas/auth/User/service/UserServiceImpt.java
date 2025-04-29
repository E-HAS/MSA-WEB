package com.ehas.auth.User.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.auth.User.dto.UserDto;
import com.ehas.auth.User.entity.UserEntity;
import com.ehas.auth.User.entity.UserRoleEntity;
import com.ehas.auth.User.entity.UserRoleEntityKey;
import com.ehas.auth.User.reactive.ReactiveUserRepository;
import com.ehas.auth.User.reactive.ReactiveUserRoleRepository;
import com.ehas.auth.User.userstatus.UserStatus;
import com.ehas.auth.content.entity.RoleEntity;
import com.ehas.auth.content.reactive.ReactiveRoleRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final ReactiveUserRepository ReactiveUserRepo;
	private final ReactiveUserRoleRepository ReactiveUserRoleRepo;
	private final ReactiveRoleRepository reactiveRoleRepository;
	
	@Transactional(rollbackFor = { Exception.class })  
	public Mono<Boolean> saveByUserEntity(UserDto user, Integer contentSeq, Integer roleSeq){
		return ReactiveUserRepo.save(UserEntity.builder()
										.id(user.getId())
										.password(user.getPassword())
										.name(user.getName())
										.status(UserStatus.ACTIVE)
										.build())
				.flatMap(saveEntity -> ReactiveUserRepo.findById(saveEntity.getId()))
				.flatMap(userEntity -> ReactiveUserRoleRepo.save(UserRoleEntity
																.builder()
																.contentSeq(contentSeq)
																.userSeq(userEntity.getSeq())
																.roleSeq(roleSeq)
																.build())
																.thenReturn(true))
				.thenReturn(true)
				.onErrorReturn(false);
	}
	
	
	public Mono<UserEntity> findByUserSeq(Integer seq){
		return ReactiveUserRepo.findBySeq(seq);
	}
	
	public Mono<UserEntity> findByUserId(String id){
		return ReactiveUserRepo.findById(id);
	}
	
	public Flux<UserRoleEntity> findByUserRoleSeq(Integer seq){
		return ReactiveUserRoleRepo.findBySeq(seq);
	}
	
	public Flux<RoleEntity> findRoleByUserSeq(Integer seq){
		return reactiveRoleRepository.findByUserSeq(seq);
	}
}
