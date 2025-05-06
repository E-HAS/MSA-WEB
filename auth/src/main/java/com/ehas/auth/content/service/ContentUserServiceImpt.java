package com.ehas.auth.content.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ehas.auth.User.reactive.ReactiveRoleRepository;
import com.ehas.auth.User.reactive.ReactiveUserRepository;
import com.ehas.auth.User.reactive.ReactiveUserRoleRepository;
import com.ehas.auth.User.service.UserServiceImpt;
import com.ehas.auth.User.userstatus.UserStatus;
import com.ehas.auth.content.dto.ContentUserDto;
import com.ehas.auth.content.entity.ContentUserEntity;
import com.ehas.auth.content.reactive.ReactiveContentUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentUserServiceImpt {
	private final ReactiveContentUserRepository reactiveContentUsersRepository;
	
	public Mono<ContentUserEntity> findBySeq(Integer contentUsersSeq){
		return reactiveContentUsersRepository.findByContentUserSeq(contentUsersSeq);
	}
	
	public Flux<ContentUserEntity> findByContentSeq(Integer contentSeq){
		return reactiveContentUsersRepository.findByContentSeq(contentSeq);
	}
	
	public Mono<ContentUserEntity> findByContentSeqAndUserSeq(Integer contentSeq, Integer userSeq){
		return reactiveContentUsersRepository.findByContentSeqAndUserSeq(contentSeq, userSeq);
	}
	
	public Mono<ContentUserEntity> findByContentSeqAndUserName(Integer contentSeq, String userName){
		return reactiveContentUsersRepository.findByContentSeqAndUserName(contentSeq, userName);
	}
	
	public Mono<Boolean> addContentUser(ContentUserDto contentUserDto){
		return reactiveContentUsersRepository.save(ContentUserEntity.builder()
																	.contentSeq(contentUserDto.getContentSeq())
																	.userSeq(contentUserDto.getUserSeq())
																	.userName(contentUserDto.getUserName())
																	.userDept(contentUserDto.getUserDept())
																	.status(UserStatus.INACTIVE)
																	.registeredDate(LocalDateTime.now())
																	.build())
												.thenReturn(true)
												.onErrorReturn(false);
	}
	
	public Mono<Boolean> updateContentUser(ContentUserDto contentUserDto){
		return reactiveContentUsersRepository.updateByContentUserSeq(contentUserDto.getSeq()
																	, contentUserDto.getUserName()
																	, contentUserDto.getUserDept()
																	, contentUserDto.getStatus()
																	, LocalDateTime.now())
											.thenReturn(true)
											.onErrorReturn(false);
	}
	
	public Mono<Boolean> deleteContentUser(ContentUserDto contentUserDto){
		return reactiveContentUsersRepository.deleteByContentUserSeq(contentUserDto.getSeq()
																	, contentUserDto.getStatus()
																	, LocalDateTime.now())
											.thenReturn(true)
											.onErrorReturn(false);
	}
	
	public Mono<Boolean> deleteBySeq(Integer contentUserSeq){
		return reactiveContentUsersRepository.deleteById(contentUserSeq)
												.thenReturn(true)
												.onErrorReturn(false);
	}
}
