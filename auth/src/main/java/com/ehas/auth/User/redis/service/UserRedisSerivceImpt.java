package com.ehas.auth.User.redis.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ehas.auth.User.reactive.ReactiveUserRepository;
import com.ehas.auth.User.redis.dto.RedisUserDto;
import com.ehas.auth.redis.service.CacheRedisHashService;
import com.ehas.auth.redis.service.CacheRedisService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final String prefixUser= "user:";
    private final Integer duration= 60;
 // 저장
    public Mono<Boolean> save(String userId, RedisUserDto value) {
        return cacheRedisService.save(prefixUser,userId,value.toString()
        												,Duration.ofMinutes(duration));
    }

    // 조회
    public Mono<RedisUserDto> get(String userId) {
        return cacheRedisService.get(prefixUser, userId)
        		.map(obj -> new ObjectMapper().convertValue(obj, RedisUserDto.class));
    }

    // 삭제
    public Mono<Boolean> delete(String userId) {
        return cacheRedisService.delete(prefixUser, userId);
    }

    // 존재 여부 확인
    public Mono<Boolean> exists(String userId) {
        return cacheRedisService.exists(prefixUser, userId);
    }
    
    public Mono<RedisUserDto> findByUserId(String id) {
        return this.exists(id) // 또는 this.exists(prefixUser + id)
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    return this.get(id);
                } else {
                    return ReactiveUserRepo.getUserById(id)
                        .flatMap(dto -> {
                            RedisUserDto redisUserDto = RedisUserDto.builder()
                                .userSeq(dto.getSeq())
                                .addressSeq(dto.getAddressSeq())
                                .name(dto.getName())
                                .Status(dto.getStatus())
                                .build();

                            return this.save(id, redisUserDto)
                                .flatMap(result -> {
                                    if (result) {
                                        return Mono.just(redisUserDto);
                                    } else {
                                        return Mono.error(new RuntimeException("Failed to save to Redis"));
                                    }
                                });
                        });
                }
            });
    }
}
