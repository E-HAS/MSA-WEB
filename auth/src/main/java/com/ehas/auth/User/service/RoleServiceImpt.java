package com.ehas.auth.User.service;

import org.springframework.stereotype.Service;

import com.ehas.auth.User.dto.RoleDto;
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
public class RoleServiceImpt {
	private final ReactiveRoleRepository reactiveRoleRepository;
	
	public Mono<RoleEntity> findRole(Integer seq){
		return reactiveRoleRepository.findById(seq);
	}
	
	public Flux<RoleEntity> findAllRole(){
		return reactiveRoleRepository.findAll();
	}
	
	public Mono<Boolean> addRole(RoleDto roleDto){
		return reactiveRoleRepository.save(RoleEntity.builder()
													.roleName(roleDto.getRoleName())
													.roleDept(roleDto.getRoleDept())
													.build())
									 .thenReturn(true)
									 .onErrorReturn(false);
	}
	
	public Mono<Boolean> ModifyRole(RoleDto roleDto){
		return reactiveRoleRepository.updateByseq(roleDto.getSeq()
												,roleDto.getRoleName()
												,roleDto.getRoleDept())
									 .thenReturn(true)
									 .onErrorReturn(false);
	}
	
	
	public Mono<Boolean> deleteRole(Integer seq){
		return reactiveRoleRepository.deleteById(seq)
									 .thenReturn(true)
									 .onErrorReturn(false);
	}

	
	public Flux<RoleEntity> findRoleByUserSeq(Integer userSeq){
		return reactiveRoleRepository.findByUserSeq(userSeq);
	}
	
	public Flux<RoleEntity> findRoleByUserId(String userId){
		return reactiveRoleRepository.findByUserId(userId);
	}
}
