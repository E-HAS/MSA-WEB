package com.ehas.auth.api;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	private final JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/create/{id}")
	public String create(@PathVariable String id){
    	User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        UserDetails rob = userBuilder.username(id)
                .password("password")
                .roles("USER")
                .build();
		Authentication authentication =	new UsernamePasswordAuthenticationToken(rob, "", rob.getAuthorities());
		String token = jwtTokenProvider.createToken(authentication);
		return token;
	}
	
	@GetMapping("/user")
	public Mono<UserEntity> getUser(){
		String uid = "60055614d9df4e1bb7a1cebd9f5a101d";
		Mono<UserEntity> user = userServiceImpt.findByIdRx(uid).delayElement(Duration.ofSeconds(1)).log();
		return user;
	}

}
