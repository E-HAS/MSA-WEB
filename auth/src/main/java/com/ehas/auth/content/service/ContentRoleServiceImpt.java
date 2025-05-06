package com.ehas.auth.content.service;

import org.springframework.stereotype.Service;

import com.ehas.auth.content.dto.ContentRoleDto;
import com.ehas.auth.content.entity.ContentRoleEntity;
import com.ehas.auth.content.reactive.ReactiveContentRoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentRoleServiceImpt {
	private final ReactiveContentRoleRepository reactiveContentRoleRepository;

	public Mono<ContentRoleEntity> findBySeq(Integer seq){
		return reactiveContentRoleRepository.findByContentRoleSeq(seq);		
	}
	
	public Flux<ContentRoleEntity> findByContentSeq(Integer contentSeq){
		return reactiveContentRoleRepository.findByContentSeq(contentSeq);		
	}
	
	public Mono<ContentRoleEntity> findByContentSeqAndRoleName(Integer contentSeq, String roleName){
		return reactiveContentRoleRepository.findByContentSeqAndRoleName(contentSeq, roleName);
	}
	
	public Mono<Boolean> addContentRole(ContentRoleDto contentRoleDto){
		return reactiveContentRoleRepository.save(ContentRoleEntity.builder()
																	.contentSeq(contentRoleDto.getContentSeq())
																	.roleName(contentRoleDto.getRoleName())
																	.roleDept(contentRoleDto.getRoleDept())
																	.build())
											.thenReturn(true)
											.onErrorReturn(false);
	}
	
	public Mono<Boolean> updateContentRole(ContentRoleDto contentRoleDto){
		return reactiveContentRoleRepository.updateByContentRoleSeq(contentRoleDto.getSeq()
																	,contentRoleDto.getRoleName()
																	,contentRoleDto.getRoleDept())
											.thenReturn(true)
											.onErrorReturn(false);
	}
	
	public Mono<Boolean> deleteContentRole(Integer contentRoleSeq){
		return reactiveContentRoleRepository.deleteById(contentRoleSeq)
											.thenReturn(true)
											.onErrorReturn(false);
	}


}
