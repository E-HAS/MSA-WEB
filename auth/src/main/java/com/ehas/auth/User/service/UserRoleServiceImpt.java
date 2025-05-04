package com.ehas.auth.User.service;

import org.springframework.stereotype.Service;

import com.ehas.auth.User.dto.UserRoleDto;
import com.ehas.auth.User.entity.RoleEntity;
import com.ehas.auth.User.entity.UserRoleEntity;
import com.ehas.auth.User.entity.UserRoleEntityKey;
import com.ehas.auth.User.reactive.ReactiveRoleRepository;
import com.ehas.auth.User.reactive.ReactiveUserRepository;
import com.ehas.auth.User.reactive.ReactiveUserRoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpt {
	private final ReactiveUserRoleRepository reactiveUserRoleRepository;
	private final ReactiveRoleRepository reactiveRoleRepository;
	
	public Mono<UserRoleEntity> addRole(UserRoleDto userRoleDto){
		return reactiveUserRoleRepository.save(UserRoleEntity.builder()
														.userSeq(userRoleDto.getUserSeq())
														.roleSeq(userRoleDto.getRoleSeq())
														.build());
	}
	
	public Mono<Boolean> deleteRole(UserRoleDto userRoleDto){
		return reactiveUserRoleRepository.deleteById(UserRoleEntityKey.builder()
														.userSeq(userRoleDto.getUserSeq())
														.roleSeq(userRoleDto.getRoleSeq())
														.build())
				.thenReturn(true)
				.onErrorReturn(false);
	}
	
	public Mono<Boolean> deleteRoleByUserIdAndRoleSeq(String userId, Integer roleSeq){
		return reactiveUserRoleRepository.findByUserIdAndRoleSeq(userId, roleSeq)
				.thenReturn(true)
				.onErrorReturn(false);
	}
	
	public Flux<UserRoleEntity> findByUserSeq(Integer seq){
		return reactiveUserRoleRepository.findByUserSeq(seq);
	}
	
	public Flux<RoleEntity> findRoleByUserSeq(Integer userSeq){
		return reactiveRoleRepository.findByUserSeq(userSeq);
	}
	
	public Flux<RoleEntity> findRoleByUserId(String userId){
		return reactiveRoleRepository.findByUserId(userId);
	}
	
}
