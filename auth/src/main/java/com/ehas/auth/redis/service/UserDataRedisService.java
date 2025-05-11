package com.ehas.auth.redis.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.ehas.auth.address.reactive.ReactiveAddressRepository;
import com.ehas.auth.redis.dto.RedisUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
public class UserDataRedisService {
	private final String userSeqKey;
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public UserDataRedisService(@Qualifier("dataRedisTemplate")ReactiveRedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.userSeqKey = "userSeq:";
    }
    
    // 저장
    public Mono<Boolean> save(String key, RedisUserDto value) {
        return redisTemplate.opsForValue().set(key, value);
    }
    
    public Mono<RedisUserDto> getUserBySave(String key, RedisUserDto value) {
        return redisTemplate.opsForValue().set(key, value)
        								  .flatMap(result ->{ 
        									  if(Boolean.TRUE.equals(result)) {
        										  return Mono.just(value);
        									  }else {
        										  return Mono.empty();
        									  }
        								  });
    }

    // 조회
    public Mono<RedisUserDto> get(String key) {
        return redisTemplate.opsForValue().get(key)
        		.map(obj -> new ObjectMapper().convertValue(obj, RedisUserDto.class));
    }

    // 삭제
    public Mono<Boolean> delete(String key) {
        return redisTemplate.delete(key)
                .map(count -> count > 0);
    }

    // 존재 여부 확인
    public Mono<Boolean> exists(String key) {
        return redisTemplate.hasKey(key);
    }
}