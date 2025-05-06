package com.ehas.auth.address.service;

import org.springframework.stereotype.Service;

import com.ehas.auth.address.entity.AddressEntity;
import com.ehas.auth.address.reactive.ReactiveAddressRepository;
import com.ehas.auth.content.reactive.ReactiveContentRoleRepository;
import com.ehas.auth.content.service.ContentRoleServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpt {
	private final ReactiveAddressRepository reactiveAddressRepository;
	
	public Flux<AddressEntity> findAll(){
		return reactiveAddressRepository.findAll();
	}
	
	public Mono<AddressEntity> findBySeq(Integer addressSeq){
		return reactiveAddressRepository.findByAddressSeq(addressSeq);
	}
	
}
