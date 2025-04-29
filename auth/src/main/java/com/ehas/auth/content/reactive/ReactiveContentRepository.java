package com.ehas.auth.content.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.content.entity.ContentEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveContentRepository extends R2dbcRepository<ContentEntity, Integer>{
	@Query(   "SELECT * "
			+ "FROM CONTENT "
			+ "WHERE SEQ = :conentSeq")
	Mono<ContentEntity> findByContentSeq(Integer conentSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT "
			+ "WHERE PARENT_SEQ = :parentSeq ")
	Flux<ContentEntity> findByParentSeq(Integer parentSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT "
			+ "WHERE PARENT_SEQ = :parentSeq "
			+ "AND SEQ = :conentSeq")
	Mono<ContentEntity> findByParentSeqAndContentSeq(Integer parentSeq, Integer conentSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT "
			+ "WHERE CONTENT_NAME = :contentName ")
	Mono<ContentEntity> findByContentName(String contentName);
}
