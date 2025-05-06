package com.ehas.auth.content.reactive;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.ehas.auth.User.userstatus.UserStatus;
import com.ehas.auth.content.entity.ContentRoleEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveContentRoleRepository extends R2dbcRepository<ContentRoleEntity, Integer>{
	@Query(   "SELECT * "
			+ "FROM CONTENT_ROLE "
			+ "WHERE SEQ = :seq")
	Mono<ContentRoleEntity> findByContentRoleSeq(Integer seq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_ROLE "
			+ "WHERE CONTENT_SEQ = :contentSeq")
	Flux<ContentRoleEntity> findByContentSeq(Integer contentSeq);
	
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_ROLE "
			+ "WHERE CONTENT_SEQ = :contentSeq "
			+ "AND ROLE_NAME = :roleName")
	Mono<ContentRoleEntity> findByContentSeqAndRoleName(Integer contentSeq, String roleName);

    @Modifying
    @Query("""
	        UPDATE CONTENT_ROLE 
	        SET 
	            ROLE_NAME = :roleName,
	            ROLE_DEPT = :roleDept
	        WHERE seq = :seq
		    """)
    Mono<Boolean> updateByContentRoleSeq(
    	    @Param("seq") Integer seq,
    	    @Param("roleName") String userName,
    	    @Param("roleDept") String userDept
    );
}
