package com.ehas.content.user.redis.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@RedisHash("user_cache")
public class RedisUserDto implements Serializable{
	private Integer userSeq;
	private Integer addressSeq;
	private String roleSeq;
	
	private String name;
	private Integer Status;
}
