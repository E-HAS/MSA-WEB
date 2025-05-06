package com.ehas.auth.content.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ehas.auth.content.dto.ContentDto;
import com.ehas.auth.content.entity.ContentEntity;
import com.ehas.auth.content.reactive.ReactiveContentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentServiceImpt {
	private final ReactiveContentRepository reactiveContentRepository;
	
	public Flux<ContentEntity> findAll(){
		return reactiveContentRepository.findAll();
	}
	
	public Mono<ContentEntity> findBySeq(Integer conentSeq){
		return reactiveContentRepository.findByContentSeq(conentSeq);
	}
	
	public Mono<ContentEntity> findByContentName(String contentName){
		return reactiveContentRepository.findByContentName(contentName);
	}
	
	public Mono<Boolean> addContent(ContentDto contentDto){
		return reactiveContentRepository.save(ContentEntity.builder()
															.contentName(contentDto.getContentName())
															.contentDept(contentDto.getContentDept())
															.used(contentDto.getUsed())
															.registeredDate(LocalDateTime.now())
															.build())
										.thenReturn(true)
										.onErrorReturn(false);
	}
	
	public Mono<Boolean> updateContent(ContentDto contentDto){
		return reactiveContentRepository.UpdateByContentSeq(contentDto.getSeq()
															, contentDto.getContentName()
															, contentDto.getContentDept()
															, contentDto.getUsed()
															, LocalDateTime.now())
															.thenReturn(true)
															.onErrorReturn(false);
	}
	
	public Mono<Boolean> deleteContent(Integer contentSeq){
		return reactiveContentRepository.deleteById(contentSeq)
										.thenReturn(true)
										.onErrorReturn(false);
	}
}
