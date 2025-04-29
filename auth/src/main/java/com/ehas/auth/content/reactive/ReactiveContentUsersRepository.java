package com.ehas.auth.content.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.content.entity.ContentUsersEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveContentUsersRepository extends R2dbcRepository<ContentUsersEntity, Integer>{
	@Query(   "SELECT * "
			+ "FROM CONTENT_USERS "
			+ "WHERE SEQ = :contentUsersseq")
	Mono<ContentUsersEntity> findByContentUsersSeq(Integer contentUsersseq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USERS "
			+ "WHERE CONTENT_SEQ = :contentSeq")
	Flux<ContentUsersEntity> findByContentSeq(Integer contentSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USERS "
			+ "WHERE CONTENT_SEQ = :contentSeq "
			+ "AND USER_SEQ = :userSeq")
	Flux<ContentUsersEntity> findByContentSeqAndUserSeq(Integer contentSeq, Integer userSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USERS "
			+ "WHERE CONTENT_SEQ = :contentSeq "
			+ "AND USER_Name = :userName")
	Flux<ContentUsersEntity> findByContentSeqAndUserName(Integer contentSeq, String userName);
}