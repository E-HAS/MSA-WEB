package com.ehas.auth.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.service.UserServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cms")
public class CmsRestController {
	private final UserServiceImpt userServiceImpt;
	
	@GetMapping("/user")
	public Mono<UserEntity> getUser(){
		String uid = "60055614d9df4e1bb7a1cebd9f5a101d";
		Mono<UserEntity> user = userServiceImpt.findByIdRx(uid);
		return user;
	}

}