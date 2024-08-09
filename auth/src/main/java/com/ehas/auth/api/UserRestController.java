package com.ehas.auth.api;

import java.time.Duration;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.handler.UserHandler;
import com.ehas.auth.jwt.JwtTokenProvider;
import com.ehas.auth.service.UserServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class UserRestController {
	private final UserServiceImpt userServiceImpt;
	
	private final ReactiveAuthenticationManager reactiveAuthenticationManager;
	private final ReactiveUserDetailsService reactiveUserDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	
	private final UserHandler userHandler;
	
	@GetMapping("/test")
	//@PreAuthorize("@UserHandler.getTest()")
	@PreAuthorize("hasRole('ADMIN')")
	public Mono<UserEntity> test(){
		userHandler.getTest();
		//return userServiceImpt.findByIdRxTest("60055614d9df4e1bb7a1cebd9f5a101d").log();
		return userServiceImpt.findByIdRx("60055614d9df4e1bb7a1cebd9f5a101d").log();
	}
	
	@GetMapping("/create/{id}")
	public Mono<String> create(@PathVariable String id){
		Mono<String> token =  userServiceImpt.findByIdRx(id)
												.map(v->{
													System.out.println(">>>> create ing :"+v.getUsername()+", "+v.getUserPassword());
													Authentication authentication = new UsernamePasswordAuthenticationToken(v.getUsername(), v.getUserPassword());
													Authentication auth = (Authentication) reactiveAuthenticationManager.authenticate(authentication).subscribe();
													return jwtTokenProvider.createToken(auth);
												}).log();
		System.out.println(">>>> create ing :"+token.log());
		return token;
		
		/*
		Mono<UserEntity> entity = userServiceImpt.findByIdRx(id).log();
		Authentication authentication = new UsernamePasswordAuthenticationToken(entity.subscribe(v->v.getUsername()), entity.subscribe(v->v.getUserPassword()));
		Mono<String> token = reactiveAuthenticationManager.authenticate(authentication)
													.map(jwtTokenProvider::createToken);
		
		retur`n token;
		*/
		
		/*
		return userServiceImpt.findByIdRx(id)
																.map(v-> new UsernamePasswordAuthenticationToken(v.getUsername(), v.getUserPassword()))
																.map(reactiveAuthenticationManager::authenticate)
																.map(jwtTokenProvider::createTokenRx)
																.flatMap(v -> v)
																.doOnNext(System.out::println).log();
																//.subscribe(v-> v.subscribe(this::print));*/
		//Mono<UserEntity> entity = userServiceImpt.findByIdRx(id).log();
        //Authentication authentication = new UsernamePasswordAuthenticationToken(entity.subscribe(v->v.getUsername()), entity.subscribe(v->v.getUserPassword()));
        //System.out.println(">>>> find Authentication : "+authentication);
		//Mono<String> token = reactiveAuthenticationManager.authenticate(authentication)
        //        	   .map(jwtTokenProvider::createToken);
	}
	@GetMapping("/info/{id}")
	public Mono<UserDetails> getInfo(@PathVariable String id){
		return reactiveUserDetailsService.findByUsername(id);
	}
	
	@GetMapping("/user")
	public Mono<UserEntity> getUser(){
		String uid = "60055614d9df4e1bb7a1cebd9f5a101d";
		Mono<UserEntity> user = userServiceImpt.findByIdRx(uid).delayElement(Duration.ofSeconds(1)).log();
		return user;
	}

}
