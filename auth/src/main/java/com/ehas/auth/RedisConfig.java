package com.ehas.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name = "dataRedisTemplate")
    public ReactiveRedisTemplate<String, Object> reactiveDataRedisTemplate() {
    	RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("192.168.1.101", 6379);
    	config.setDatabase(0);
    	config.setPassword(RedisPassword.of("QWas1234"));
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet(); // 중요

        RedisSerializationContext<String, Object> context = RedisSerializationContext
                .<String, Object>newSerializationContext(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .value(new Jackson2JsonRedisSerializer<>(Object.class))   
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
    
    @Bean(name = "cacheRedisTemplate")
    public ReactiveRedisTemplate<String, String> reactiveCacheRedisTemplate() {
    	RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("192.168.1.101", 6379);
    	config.setDatabase(1);
    	config.setPassword(RedisPassword.of("QWas1234"));
    	LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
    	factory.afterPropertiesSet(); // 중요

        RedisSerializationContext<String, String> context = RedisSerializationContext
                .<String, String>newSerializationContext(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .value(new StringRedisSerializer())
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
