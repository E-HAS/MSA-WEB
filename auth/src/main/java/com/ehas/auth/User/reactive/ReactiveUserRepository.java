package com.ehas.auth.User.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.ehas.auth.User.entity.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveUserRepository extends R2dbcRepository<UserEntity, Integer>{
	@Query("SELECT * FROM USER WHERE seq = :seq")
	Mono<UserEntity> findBySeq(Integer seq);
	
	@Query("SELECT * FROM USER WHERE id = :id")
	Mono<UserEntity> findById(String id);
	
	@Query(   " SELECT * "
			+ " FROM USER "
			+ " WHERE status = :status "
			+ " AND id = :id")
	Flux<UserEntity> findByStatusAndId(String status, String id);
	
}
