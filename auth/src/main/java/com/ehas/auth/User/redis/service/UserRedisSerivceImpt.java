package com.ehas.auth.User.redis.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ehas.auth.User.reactive.ReactiveUserRepository;
import com.ehas.auth.redis.dto.RedisUserDto;
import com.ehas.auth.redis.service.CacheRedisHashService;
import com.ehas.auth.redis.service.CacheRedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRedisSerivceImpt {
	private final ReactiveUserRepository ReactiveUserRepo;
	private final CacheRedisHashService cacheRedisHashService;
	private final CacheRedisService cacheRedisService;
	
	public Mono<Boolean> addRefreshTokenToRedis(String token, Map<String, String> map){
		return cacheRedisService.setValue("refreshToken:",token
														 ,map.toString(), Duration.ofDays(7));
	}
	
	public Mono<RedisUserDto> findByUserIdToRedis(String id){
		String userSeqFieldName = "userSeq";
		String addressSeqFieldName = "addressSeq";
		String nameFieldName = "name";
		
		return cacheRedisHashService.existsByKeyAndField(id, userSeqFieldName) // redis key 존재 여부
		        .flatMap(exists -> {
		            if (Boolean.TRUE.equals(exists)) {
		                return cacheRedisHashService.findAllByKeyAndField(id)   // redis 캐시 데이터 가져오기
		                    .flatMap(dto -> Mono.just(RedisUserDto.builder()
		                        .userSeq(Integer.parseInt(dto.get(userSeqFieldName)))
		                        .addressSeq(Integer.parseInt(dto.get(addressSeqFieldName)))
		                        .name(dto.get(nameFieldName))
		                        .build()));
		            } else {
		                return ReactiveUserRepo.getUserById(id)   // db 데이터 가져오기
		                    .flatMap(dto -> {
		                    	// redis 캐시 데이터 넣기
		                    	return cacheRedisHashService.addAllByKeyAndMap(id
		                    											, Map.of(userSeqFieldName, dto.getSeq().toString()
		                    													,addressSeqFieldName, dto.getAddressSeq().toString()
		                    													,nameFieldName, dto.getName().toString())
		                    													, Duration.ofMinutes(60))
		                    			.thenReturn(RedisUserDto.builder()
			                                .userSeq(dto.getSeq())
			                                .addressSeq(dto.getAddressSeq())
			                                .name(dto.getName())
			                                .build()
			                             );
		                    });
		            }
		        });
	}
}
