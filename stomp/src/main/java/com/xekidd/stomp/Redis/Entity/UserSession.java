package com.xekidd.stomp.Redis.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RedisHash(value ="UserSession", timeToLive = 30) // options: timeToLive = 10
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
	@Id
	private String sessionId;
	@Indexed
	private String userName;
	private int sessionType;
	
}
