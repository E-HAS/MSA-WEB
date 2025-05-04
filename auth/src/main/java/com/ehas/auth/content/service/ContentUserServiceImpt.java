package com.ehas.auth.content.service;

import org.springframework.stereotype.Service;

import com.ehas.auth.User.reactive.ReactiveRoleRepository;
import com.ehas.auth.User.reactive.ReactiveUserRepository;
import com.ehas.auth.User.reactive.ReactiveUserRoleRepository;
import com.ehas.auth.User.service.UserServiceImpt;
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
	
	public Flux<ContentUserEntity> findByContentSeqAndUserSeq(Integer contentSeq, Integer userSeq){
		return reactiveContentUsersRepository.findByContentSeqAndUserSeq(contentSeq, userSeq);
	}
	
	public Flux<ContentUserEntity> findByContentSeqAndUserName(Integer contentSeq, String userName){
		return reactiveContentUsersRepository.findByContentSeqAndUserName(contentSeq, userName);
	}
}
