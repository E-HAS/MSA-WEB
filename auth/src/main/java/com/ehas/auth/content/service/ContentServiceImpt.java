package com.ehas.auth.content.service;

import org.springframework.stereotype.Service;

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
}
