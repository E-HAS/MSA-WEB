package com.ehas.auth.content.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.content.entity.ContentUserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveContentUserRepository extends R2dbcRepository<ContentUserEntity, Integer>{
	@Query(   "SELECT * "
			+ "FROM CONTENT_USERS "
			+ "WHERE SEQ = :contentUsersSeq")
	Mono<ContentUserEntity> findByContentUserSeq(Integer contentUsersSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USERS "
			+ "WHERE CONTENT_SEQ = :contentSeq")
	Flux<ContentUserEntity> findByContentSeq(Integer contentSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USERS "
			+ "WHERE CONTENT_SEQ = :contentSeq "
			+ "AND USER_SEQ = :userSeq")
	Flux<ContentUserEntity> findByContentSeqAndUserSeq(Integer contentSeq, Integer userSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USERS "
			+ "WHERE CONTENT_SEQ = :contentSeq "
			+ "AND USER_NAME = :userName")
	Flux<ContentUserEntity> findByContentSeqAndUserName(Integer contentSeq, String userName);
}