package com.ehas.auth.content.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.content.entity.ContentRoleEntity;
import com.ehas.auth.content.entity.ContentRoleEntityKey;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveContentRoleRepository extends R2dbcRepository<ContentRoleEntity, ContentRoleEntityKey>{
	@Query(   "SELECT * "
			+ "FROM CONTENTROLE "
			+ "WHERE CONTENT_SEQ = :contentSeq")
	Flux<ContentRoleEntity> findByContentSeq(Integer contentSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENTROLE "
			+ "WHERE CONTENT_SEQ = :contentSeq "
			+ "AND ROLE_SEQ = :roleSeq")
	Mono<ContentRoleEntity> findByContentSeqAndRoleSeq(Integer contentSeq, Integer roleSeq);
}
