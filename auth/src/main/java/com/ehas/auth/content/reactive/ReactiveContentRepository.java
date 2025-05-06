package com.ehas.auth.content.reactive;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

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
			+ "WHERE CONTENT_NAME = :contentName ")
	Mono<ContentEntity> findByContentName(String contentName);
	
	@Modifying
	@Query("""
			UPDATE CONTENT
			SET CONTENT_NAME = :contentName
				,CONTENT_DEPT = :contentDept
				,USED = :used
				,UPDATED_DATE = :updatedDate
			WHERE SEQ = :seq
			""")
	Mono<Boolean> UpdateByContentSeq(@Param("seq") Integer contentSeq
									,@Param("contentName") String contentName
									,@Param("contentDept") String contentDept
									,@Param("used") Boolean used
									,@Param("updatedDate") LocalDateTime updatedDate);
}
