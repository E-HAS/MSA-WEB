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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.auth.User.dto.ResponseDto;
import com.ehas.auth.User.dto.UserDto;
import com.ehas.auth.User.dto.UserTokenDto;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {
	private final UserServiceImpt userServiceImpt;
	
	private final ReactiveAuthenticationManager reactiveAuthenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping(path="/{userId}/token")
	public Mono<ResponseEntity<ResponseDto>> getToken( @PathVariable ("userId") String userId
													 , @RequestBody UserDto userDto
													 ){
		// 인증을 위한 authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDto.getId(), userDto.getPassword());
        
        return reactiveAuthenticationManager.authenticate(authentication)
                .flatMap(auth -> {
                    String refreshToken = jwtTokenProvider.createRefreshToken(userId); // refreshToken 발급
                    String accessToken = jwtTokenProvider.createToken(auth); // accessToken 발급
                    return userServiceImpt.addUserRefreshToken(userId, refreshToken)  // Redis에 refreshToken 발급
                            .flatMap(success -> {
                                if (Boolean.TRUE.equals(success)) {
                                    UserTokenDto tokenDto = UserTokenDto.builder()
                                            .refreshToken(refreshToken)
                                            .accessToken(accessToken)
                                            .build();
                                    return Mono.just(tokenDto);
                                } else {
                                    return Mono.error(new RuntimeException("Failed to store tokens in Redis"));
                                }
                            });
                })
                .map(token -> {
                    return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ResponseDto.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(HttpStatus.CREATED.getReasonPhrase())
                            .data(Map.of("token", token))
                            .build());
                })
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ResponseDto.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .build()));
                });
	}
	
	@PostMapping(path="/{userId}/token/refresh")
	public Mono<ResponseEntity<ResponseDto>> getRefreshToken( @RequestHeader("Authorization") String header
															, @PathVariable ("userId") String userId){
		String refreshToken = header.replace("Bearer ", "");
		
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseDto.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .build()));
	}
	
	@PostMapping
	public Mono<ResponseEntity<ResponseDto>> registerUser(@RequestBody UserDto userDto){
		
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		return userServiceImpt.saveByUser(userDto)
								.map(result ->{ return result ? ResponseEntity.status(HttpStatus.CREATED)
																								.body(ResponseDto.builder()
																								.status(HttpStatus.CREATED.value())
																								.message(HttpStatus.CREATED.getReasonPhrase())
																								.build())
															  : ResponseEntity.status(HttpStatus.BAD_REQUEST)
															  									.body(ResponseDto.builder()
																	  							.status(HttpStatus.BAD_REQUEST.value())
																	  							.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																	  							.build());
								});
	}
	
	@GetMapping(path="/{userId}")
	public Mono<ResponseEntity<ResponseDto>> getUser(@PathVariable ("userId") String userId){
		return userServiceImpt.findByUserId(userId)
					.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
																					.body(ResponseDto.builder()
																					.status(HttpStatus.OK.value())
																					.message(HttpStatus.OK.getReasonPhrase())
																					.data(Map.of("user", findEntity))
																					.build())
																	: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																						.body(ResponseDto.builder()
																						.status(HttpStatus.BAD_REQUEST.value())
																						.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																						.build());
					})
					.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(ResponseDto.builder()
							 .status(HttpStatus.BAD_REQUEST.value())
							 .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
							 .build()));
	}
	
	@PutMapping(path="/{userId}")
	public Mono<ResponseEntity<ResponseDto>> updateUser(@PathVariable ("userId") String userId
													   ,@RequestBody UserDto userDto
													 	){
		
		if(!userServiceImpt.isStringNullOrEmpty(userDto.getPassword())) {
			userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}
		
		return userServiceImpt.updateByUser(userDto)
					.map(result ->{ return result ? ResponseEntity.status(HttpStatus.OK)
																					.body(ResponseDto.builder()
																					.status(HttpStatus.OK.value())
																					.message(HttpStatus.OK.getReasonPhrase())
																					.build())
																	: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																						.body(ResponseDto.builder()
																						.status(HttpStatus.BAD_REQUEST.value())
																						.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																						.build());
					})
					.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
												 .body(ResponseDto.builder()
												 .status(HttpStatus.BAD_REQUEST.value())
												 .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
												 .build()));
	}
	
	@DeleteMapping(path="/{userId}")
	public Mono<ResponseEntity<ResponseDto>> deleteUser(@PathVariable ("userId") String userId
													 	){
		return userServiceImpt.deleteByUser(userId)
					.map(result ->{ return result ? ResponseEntity.status(HttpStatus.NO_CONTENT)
																					.body(ResponseDto.builder()
																					.status(HttpStatus.NO_CONTENT.value())
																					.message(HttpStatus.NO_CONTENT.getReasonPhrase())
																					.build())
																	: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																						.body(ResponseDto.builder()
																						.status(HttpStatus.BAD_REQUEST.value())
																						.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																						.build());
					})
					.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
												 .body(ResponseDto.builder()
												 .status(HttpStatus.BAD_REQUEST.value())
												 .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
												 .build()));
	}
}
