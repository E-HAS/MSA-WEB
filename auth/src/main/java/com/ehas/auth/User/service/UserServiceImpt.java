package com.ehas.auth.User.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.auth.User.api.UserRestController;
import com.ehas.auth.User.dto.UserDto;
import com.ehas.auth.User.entity.RoleEntity;
import com.ehas.auth.User.entity.UserEntity;
import com.ehas.auth.User.entity.UserRoleEntity;
import com.ehas.auth.User.entity.UserRoleEntityKey;
import com.ehas.auth.User.reactive.ReactiveRoleRepository;
import com.ehas.auth.User.reactive.ReactiveUserRepository;
import com.ehas.auth.User.reactive.ReactiveUserRoleRepository;
import com.ehas.auth.User.userstatus.UserStatus;
import com.ehas.auth.redis.dto.RedisUserDto;
import com.ehas.auth.redis.service.UserDataRedisService;

import io.jsonwebtoken.lang.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final ReactiveUserRepository ReactiveUserRepo;
	private final ReactiveUserRoleRepository ReactiveUserRoleRepo;
	private final ReactiveRoleRepository reactiveRoleRepository;
	
	private final UserDataRedisService userDataRedisService;
	
	//@Transactional(rollbackFor = { Exception.class })  
	public Mono<Boolean> saveByUser(UserDto user){
		return ReactiveUserRepo.save(UserEntity.builder()
										.id(user.getId())
										.password(user.getPassword())
										.name(user.getName())
										.addressSeq(user.getAddressSeq())
										.status(UserStatus.ACTIVE)
										.registeredDate(LocalDateTime.now())
										.build())
				.flatMap(saveEntity -> this.findByUserId(saveEntity.getId()))
				.flatMap(userEntity -> ReactiveUserRoleRepo.save(UserRoleEntity
																.builder()
																.userSeq(userEntity.getSeq())
																.roleSeq(user.getRoleSeq())
																.build())
						)
				
				.thenReturn(true)
				.onErrorReturn(false);
	}
	
	//@Transactional(rollbackFor = { Exception.class })  
	public Mono<Boolean> updateByUser(UserDto userDto){
		return this.findByUserId(userDto.getId())
			   .flatMap(findEntity -> {
				   if(!isStringNullOrEmpty(userDto.getName())) {
					   findEntity.setName(userDto.getName());
				   }
				   if(!isStringNullOrEmpty(userDto.getPassword())) {
					   findEntity.setPassword(userDto.getPassword());
					   findEntity.setPasswordUpdatedDate(LocalDateTime.now());
				   }
				   if(userDto.getStatus() != null) {
					   findEntity.setStatus(UserStatus.getString(userDto.getStatus()));
				   }
				   if( userDto.getAddressSeq() != null ) {
					   findEntity.setAddressSeq(userDto.getAddressSeq());
				   }
				   findEntity.setUpdatedDate(LocalDateTime.now());
				   return this.updateByUserBySeq(findEntity);
			   }).log()
		        .onErrorResume(e -> {
		            log.error("Error updating user", e);
		            return Mono.just(false);
		            //return Mono.error(new RuntimeException("User update failed", e));
		        });
			   //.onErrorReturn(new UserEntity());
	}
	
	//@Transactional(rollbackFor = { Exception.class })  
	public Mono<Boolean> deleteByUser(String userId){
		return  ReactiveUserRepo.findById(userId)
				.flatMap(findEntity -> {
					findEntity.setStatus(UserStatus.INACTIVE);
					findEntity.setDeletedDate(LocalDateTime.now());
					return this.updateByUserBySeq(findEntity);
				})
				.thenReturn(true)
				.onErrorReturn(false);
	}
	
	
	public Mono<UserEntity> findByUserSeq(Integer seq){
		return ReactiveUserRepo.findBySeq(seq);
	}
	
	public Mono<UserEntity> findByUserId(String id){
		return ReactiveUserRepo.findById(id);
	}
	
	public Mono<UserDto> findByUserIdToRedis(String id){
		return userDataRedisService.exists(id)
							.flatMap(exit->{
								if(Boolean.TRUE.equals(exit)) {
									return userDataRedisService.get(id)
														       .map(dto -> UserDto.builder()
														    		   				 .seq(dto.getUserSeq())
														    		   				 .addressSeq(dto.getAddressSeq())
														    		   				 .build());
								}else {
									return ReactiveUserRepo.getUserById(id)
															.flatMap(dto -> {
																return userDataRedisService.getUserBySave(id
																		, RedisUserDto.builder()
																					  .userSeq(dto.getSeq())
																					  .addressSeq(dto.getAddressSeq())
																					  .name(dto.getName())
																					  .build())
											                            .flatMap(savedDto -> {
											                            return  Mono.just(
											                            		UserDto.builder()
											                                    .seq(savedDto.getUserSeq())
											                                    .addressSeq(savedDto.getAddressSeq())
											                                    .build());
											                             });
															});
								}
							});
	}
	
	public Flux<RoleEntity> findRoleByUserSeq(Integer seq){
		return reactiveRoleRepository.findByUserSeq(seq);
	}
	
	public Mono<Boolean> updateByUserBySeq(UserEntity entity){
		return ReactiveUserRepo.updateUserBySeq(entity.getSeq()
												,entity.getName()
												,entity.getPassword()
												,entity.getStatus()
												,entity.getAddressSeq()
												,entity.getPasswordUpdatedDate()
												,entity.getUpdatedDate()
												,entity.getDeletedDate());
	}
	
	public boolean isStringNullOrEmpty(String str) {
	    return str == null || str.trim().isEmpty();
	}
}
