package com.ehas.auth.redis.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.ehas.auth.address.reactive.ReactiveAddressRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CacheRedisService {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public CacheRedisService(@Qualifier("cacheRedisTemplate")ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
 // 단일 키-값 저장 + TTL 설정
    public Mono<Boolean> setValue(String prefix, String key, String value, Duration ttl) {
        String fullKey = prefix + key;
        return redisTemplate.opsForValue().set(fullKey, value)
            .flatMap(success -> {
                if (Boolean.TRUE.equals(success)) {
                    return redisTemplate.expire(fullKey, ttl);
                } else {
                    return Mono.just(false);
                }
            });
    }

    // 여러 키-값 저장 + 각 항목 TTL 설정 (주의: opsForValue에는 putAll 없음)
    public Mono<Boolean> setValues(String prefix, Map<String, String> values, Duration ttl) {
        return Flux.fromIterable(values.entrySet())
            .flatMap(entry -> {
                String fullKey = prefix + entry.getKey();
                return redisTemplate.opsForValue().set(fullKey, entry.getValue())
                    .flatMap(success -> {
                        if (Boolean.TRUE.equals(success)) {
                            return redisTemplate.expire(fullKey, ttl);
                        } else {
                            return Mono.just(false);
                        }
                    });
            })
            .all(Boolean::booleanValue); // 모두 성공했는지 여부 반환
    }

    // 단일 키 조회
    public Mono<String> getValue(String prefix, String key) {
        return redisTemplate.opsForValue().get(prefix + key);
    }

    // 단일 키 삭제
    public Mono<Boolean> deleteValue(String prefix, String key) {
        return redisTemplate.delete(prefix + key)
            .map(count -> count > 0);
    }

    // 키 존재 여부 확인
    public Mono<Boolean> existsKey(String prefix, String key) {
        return redisTemplate.hasKey(prefix + key);
    }
}