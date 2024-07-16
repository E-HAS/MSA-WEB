package com.ehas.auth.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.auth.entity.User;
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
	
	@GetMapping("/user")
	public Mono<User> getUser(){
		String uid = "60055614d9df4e1bb7a1cebd9f5a101d";
		Mono<User> user = userServiceImpt.findByIdRx(uid);
		return user;
	}

}
