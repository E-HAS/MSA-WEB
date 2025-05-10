package com.ehas.auth.redis.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.ehas.auth.address.reactive.ReactiveAddressRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
public class CacheRedisService {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public CacheRedisService(@Qualifier("cacheRedisTemplate")ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Map의 put: 필드-값 추가
    public Mono<Boolean> putToMap(String mapKey, String field, String value) {
        return redisTemplate.opsForHash().put(mapKey, field, value);
    }

    // Map의 get: 특정 필드 조회
    public Mono<String> getFromMap(String mapKey, String field) {
        return redisTemplate.opsForHash().get(mapKey, field)
                .cast(String.class); // 리턴은 Object이므로 캐스팅
    }

    // Map 전체 가져오기 (필드-값 모두)
    public Mono<Map<Object, Object>> getAllFromMap(String mapKey) {
        return redisTemplate.opsForHash().entries(mapKey).collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    // Map에서 특정 필드 삭제
    public Mono<Long> deleteFromMap(String mapKey, String field) {
        return redisTemplate.opsForHash().remove(mapKey, field);
    }
    
    // 특정 key 전체 삭제
    public Mono<Boolean> deleteMapByKey(String key) {
        return redisTemplate.delete(key)
                .map(deletedCount -> deletedCount > 0); // 삭제된 키 개수가 0보다 크면 true
    }
}