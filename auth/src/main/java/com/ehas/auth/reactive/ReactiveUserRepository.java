package com.ehas.auth.reactive;

import java.util.Optional;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.ehas.auth.entity.User;
import com.ehas.auth.entity.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUserRepository extends R2dbcRepository<User, Integer>{
	@Query("SELECT * FROM USER WHERE uid = :uid")
	Mono<UserEntity> findByRxUid(String uid);
	
	@Query("SELECT * FROM USER WHERE user_id = :user_id")
	Mono<UserEntity> findByRxUserId(String user_id);
	
}
