package com.ehas.auth.User.redis.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.auth.User.dto.ResponseDto;
import com.ehas.auth.User.redis.service.UserRedisSerivceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/cache/users")
@RequiredArgsConstructor
public class RedisUserRestController {
	private final UserRedisSerivceImpt userRedisSerivceImpt;
	
	@GetMapping(path="/{userId}")
	public Mono<ResponseEntity<ResponseDto>> getUser(@PathVariable ("userId") String userId){
		return userRedisSerivceImpt.findByUserId(userId)
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
}
