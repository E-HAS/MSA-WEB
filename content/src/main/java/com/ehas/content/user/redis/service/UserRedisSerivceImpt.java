package com.ehas.content.user.redis.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ehas.content.redis.service.CacheRedisService;
import com.ehas.content.user.dto.UserDto;
import com.ehas.content.user.redis.dto.RedisUserDto;
import com.ehas.content.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRedisSerivceImpt {
	private final UserRepository userRepository;
	private final CacheRedisService cacheRedisService;
    private final String prefixUser= "user:";
    private final Integer durationOfMin= 60;
    
 // 저장
    public Boolean save(String userId, RedisUserDto value) {
        return cacheRedisService.save(prefixUser,userId,value.toString()
        												,Duration.ofMinutes(durationOfMin));
    }

    // 조회
    public RedisUserDto get(String userId) {
    	String jsonUser = cacheRedisService.get(prefixUser, userId);
    	return new ObjectMapper().convertValue(jsonUser, RedisUserDto.class);
    }

    // 삭제
    public Boolean delete(String userId) {
        return cacheRedisService.delete(prefixUser, userId);
    }

    // 존재 여부 확인
    public Boolean exists(String userId) {
        return cacheRedisService.exists(prefixUser, userId);
    }
    
    public RedisUserDto findByUserId(String id) {
    	Boolean exists =  this.exists(id); // 또는 this.exists(prefixUser + id)
    	
        if (Boolean.FALSE.equals(exists)) {
        	UserDto userDto = userRepository.getUserById(id);
        	RedisUserDto redisUserDto = RedisUserDto.builder()
								                    .userSeq(userDto.getSeq())
								                    .addressSeq(userDto.getAddressSeq())
								                    .name(userDto.getName())
								                    .Status(userDto.getStatus())
								                    .build();
        	
        	Boolean result = this.save(id, redisUserDto);
            if (result) {
                return redisUserDto;
            } else {
                new RuntimeException("Failed to save to Redis");
            }
        }
        
        return this.get(id);
       
    }
}
