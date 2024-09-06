package com.ehas.auth.api;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.auth.dto.RequestResponseDto;
import com.ehas.auth.dto.UserDto;
import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.handler.UserHandler;
import com.ehas.auth.jwt.JwtTokenProvider;
import com.ehas.auth.service.KafkaProducerService;
import com.ehas.auth.service.UserServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthRestController {
private final UserServiceImpt userServiceImpt;
	
	private final ReactiveAuthenticationManager reactiveAuthenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	
	@PostMapping(path="/user")
	public Mono<RequestResponseDto> getUsers(@RequestBody UserDto user){
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword());
        return reactiveAuthenticationManager.authenticate(authentication)
        		.map(u -> RequestResponseDto.builder()
						.status("200")
						.message("Success")
						.data( Arrays.asList( Map.of("user",u) ))
						.build());
	}
	
	
	@PostMapping("/token/create")
	public Mono<RequestResponseDto> create(@RequestBody UserDto user){
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword());
	    return reactiveAuthenticationManager.authenticate(authentication)
                	   .map(jwtTokenProvider::createToken)
                	   .map(token-> RequestResponseDto.builder()
								.status("200")
								.message("Success")
								.data( Arrays.asList( Map.of("token",token) ))
								.build())
                	   .onErrorReturn(
                		   RequestResponseDto.builder()
							.status("400")
							.message("Bad Request")
							.build())
                	   .log();
	}
}
