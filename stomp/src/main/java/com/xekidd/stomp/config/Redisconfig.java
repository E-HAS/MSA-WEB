package com.xekidd.stomp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class Redisconfig {
	   @Value("${spring.data.redis.host}")
	   private String host;

	   @Value("${spring.data.redis.port}")
	   private int port;
	   
	   @Bean
	   public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
		redisConfiguration.setHostName(host);
		redisConfiguration.setPort(port);
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);

	       return lettuceConnectionFactory;
	   }
	   
	   @Primary
	   @Bean
	   public RedisTemplate<String, String> redisTemplate() {
	       RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
	       redisTemplate.setConnectionFactory(redisConnectionFactory());
	       redisTemplate.setKeySerializer(new StringRedisSerializer());
	       redisTemplate.setValueSerializer(new StringRedisSerializer());
	       return redisTemplate;
	   }
}
