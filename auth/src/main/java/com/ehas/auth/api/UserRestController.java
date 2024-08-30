package com.ehas.auth.api;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class UserRestController {
	private final UserServiceImpt userServiceImpt;
	
	private final ReactiveAuthenticationManager reactiveAuthenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	
	private final KafkaProducerService kafkaProducerService;
	
	private final UserHandler userHandler;
	
	private final Sinks.Many<UserDto> userSink = Sinks.many().multicast().onBackpressureBuffer();
	
	@GetMapping("/test") //, produces = "text/event-stream;charset=UTF-8" / MediaType.TEXT_EVENT_STREAM_VALUE  / SseEmitter  단방향
	//@PreAuthorize("@UserHandler.getTest()")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<UserEntity> test(){
		//userHandler.getTest();
		//return userServiceImpt.findByIdRxTest("60055614d9df4e1bb7a1cebd9f5a101d").log();
		return userServiceImpt.findByUidRx("60055614d9df4e1bb7a1cebd9f5a101d").log();
	}
	
	@GetMapping(path = "/sink/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<UserDto>> getSinkUsers(){
		return userSink.asFlux().map(u -> ServerSentEvent.builder(u).build()).doOnCancel(()->{
			userSink.asFlux().blockLast();
		});
	}
	
	@PostMapping("/sink/users")
	public Mono<UserDto> postSinkUsers() {
		return Mono.just(UserDto.builder().userName("name").userPassword("password").build()).doOnNext(u -> userSink.tryEmitNext(u));
	}
	
	@PostMapping(path="/user")
	public Mono<RequestResponseDto> getUsers(@RequestBody UserDto user){
		System.out.println(">>>> getUserInfo ing :"+user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword());
        return reactiveAuthenticationManager.authenticate(authentication)
        		.map(v -> RequestResponseDto.builder()
						.status("200")
						.message("Success")
						.data( Arrays.asList( Map.of("user",v) ))
						.build());
	}
	
	
	@PostMapping("/token/create")
	public Mono<RequestResponseDto> create(@RequestBody UserDto user){
		System.out.println(">>>> create ing :"+user);
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
	
	
	@PostMapping("/kafka/{value}")
	public Mono<Map<String, String>> postValueByKafka(@PathVariable("value") String value){
		kafkaProducerService.sendMessage(value);
		return Mono.just(Map.of("send",value));
	}
}
