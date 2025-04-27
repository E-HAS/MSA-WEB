package com.ehas.auth.User.api;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.auth.User.dto.ResponseDto;
import com.ehas.auth.User.dto.UserDto;
import com.ehas.auth.User.service.UserServiceImpt;
import com.ehas.auth.jwt.service.JwtTokenProvider;
import com.ehas.auth.kafka.service.KafkaLogProducerService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class UserRestController {
	private final UserServiceImpt userServiceImpt;
	
	private final ReactiveAuthenticationManager reactiveAuthenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	
	private final KafkaLogProducerService kafkaProducerService;
	
	//private final Sinks.Many<Map<String,Object>> userSink = Sinks.many().multicast().onBackpressureBuffer();
	
	@PostMapping(path="/{contentId}/users/{userId}")
	public Mono<ResponseEntity<ResponseDto>> getUserByContentId(@RequestBody UserDto user
											, @PathVariable ("contentId") Integer contentId
											, @PathVariable ("userId") String userId){
		// 인증을 위한 authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, user.getPassword());
        
	    return reactiveAuthenticationManager.authenticate(authentication) // 인증 후 토큰 생성
                	   .map(jwtTokenProvider::createToken)
                       .map(token -> ResponseEntity.ok().body(
                               ResponseDto.builder()
                                       .status("200")
                                       .message("Success")
                                       .data(List.of(Map.of("token", token)))  // 데이터는 리스트로 감싸서 반환
                                       .build()
                       ))
                       .onErrorReturn(
                               ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                       .body(ResponseDto.builder()
                                               .status("400")
                                               .message("Bad Request")
                                               .build()
                                       )
                       ).log();
	}
	
	@PostMapping(path="/{contentId}/users")
	public Mono<ResponseEntity<ResponseDto>> registerUserByContentId(@RequestBody UserDto user
											, @PathVariable ("contentId") Integer contentId){
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userServiceImpt.saveByUserEntity(user, contentId, user.getRoleSeq())
								.map(result ->{ return result ? ResponseEntity.ok(ResponseDto.builder()
																								.status("200")
																								.message("Sccuess")
																								.build())
															  : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder()
																	  															.status("400")
																	  															.message("Bad Request")
																	  															.build());
								}).log();
	}
	
	/*
	@GetMapping(path = "/sink/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<Map<String,Object>>> getSinkUsers(){
		return userSink.asFlux().map(u -> ServerSentEvent.builder(u).build()).doOnCancel(()->{
			userSink.asFlux().blockLast();
		});
	}
	
	/*
	@PostMapping("/sink/users")
	public Mono<Map<String,Object>> postSinkUsers(@RequestBody Map<String,Object> message) {
		return Mono.just(message).doOnNext(u -> userSink.tryEmitNext(u));
	}
	
	@PostMapping("/kafka/{value}")
	public Mono<Map<String, String>> postValueByKafka(@PathVariable("value") String value){
		kafkaProducerService.sendMessage(value);
		return Mono.just(Map.of("send",value));
	}
	*/
}
