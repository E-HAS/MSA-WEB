package com.ehas.auth.redis.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.ehas.auth.address.reactive.ReactiveAddressRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
public class CacheRedisHashService {
	private final String userKey;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public CacheRedisHashService(@Qualifier("cacheRedisTemplate")ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.userKey = "user:";
    }
    
    // 필드-값 추가
    public Mono<Boolean> addByKeyAndValue(String mapKey, String field, String value, Duration ttl) {
        return redisTemplate.opsForHash().put(this.userKey+mapKey, field, value)
						                .flatMap(success -> {
						                    if (Boolean.TRUE.equals(success)) {
						                        return redisTemplate.expire(this.userKey+mapKey, ttl);
						                    } else {
						                        return Mono.just(false);
						                    }
						                });
    }
    // 필드-값 전체 추가
    public Mono<Boolean> addAllByKeyAndMap(String mapKey, Map<String, String> values, Duration ttl) {
        String fullKey = this.userKey + mapKey;
        return redisTemplate.opsForHash()
        	 .putAll(fullKey, values)
            .flatMap(success -> {
                if (Boolean.TRUE.equals(success)) {
                    return redisTemplate.expire(fullKey, ttl);
                } else {
                    return Mono.just(false);
                }
            });
    }
    
    // 특정 필드 조회
    public Mono<String> findByKeyAndField(String mapKey, String field) {
        return redisTemplate.opsForHash().get(this.userKey+mapKey, field)
                .cast(String.class); // 리턴은 Object이므로 캐스팅
    }

    // 전체 가져오기
    public Mono<Map<String, String>> findAllByKeyAndField(String mapKey) {
        return redisTemplate.opsForHash().entries(this.userKey+mapKey).collectMap( entry -> entry.getKey().toString()
        																			, entry -> entry.getValue() != null 
        																										? entry.getValue().toString() 
        																										: "");
    }

    // 특정 필드 삭제
    public Mono<Long> deleteByKeyAndField(String mapKey, String field) {
        return redisTemplate.opsForHash().remove(this.userKey+mapKey, field);
    }
    
    // 특정 key 전체 삭제
    public Mono<Boolean> deleteByKey(String key) {
        return redisTemplate.delete(userKey+key)
                .map(deletedCount -> deletedCount > 0); // 삭제된 키 개수가 0보다 크면 true
    }
    
    // 특정 Key 필드 존재 여부
    public Mono<Boolean> existsByKeyAndField(String mapKey, String field) {
        return redisTemplate.opsForHash().hasKey(userKey + mapKey, field);
    }
}