package com.ehas.auth.reactive;

import java.util.Optional;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.ehas.auth.entity.UserEntity;

import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUserRepository extends R2dbcRepository<UserEntity, String>{
	@Query("SELECT * FROM USER WHERE uid = :uid")
	Mono<UserEntity> findByIdRx(String uid);
	
	@Query("SELECT * FROM USER WHERE userId = :userId")
	Optional<UserEntity> findByOpUserId(String userId);
	
	@Query("SELECT * FROM USER WHERE userId = :userId")
	Mono<UserEntity> findByRxUserId(String userId);
	
	@Query("SELECT * FROM USER WHERE userId = :userId")
	UserEntity findByUserId(String userId);
}
