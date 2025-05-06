package com.ehas.auth.content.reactive;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.ehas.auth.User.userstatus.UserStatus;
import com.ehas.auth.content.entity.ContentUserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveContentUserRepository extends R2dbcRepository<ContentUserEntity, Integer>{
	@Query(   "SELECT * "
			+ "FROM CONTENT_USER "
			+ "WHERE SEQ = :contentUsersSeq")
	Mono<ContentUserEntity> findByContentUserSeq(Integer contentUsersSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USER "
			+ "WHERE CONTENT_SEQ = :contentSeq")
	Flux<ContentUserEntity> findByContentSeq(Integer contentSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USER "
			+ "WHERE CONTENT_SEQ = :contentSeq "
			+ "AND USER_SEQ = :userSeq")
	Mono<ContentUserEntity> findByContentSeqAndUserSeq(Integer contentSeq, Integer userSeq);
	
	@Query(   "SELECT * "
			+ "FROM CONTENT_USER "
			+ "WHERE CONTENT_SEQ = :contentSeq "
			+ "AND USER_NAME = :userName")
	Mono<ContentUserEntity> findByContentSeqAndUserName(Integer contentSeq, String userName);

    @Modifying
    @Query("""
	        UPDATE CONTENT_USER 
	        SET 
	            USER_NAME = :userName,
	            USER_DEPT = :userDept,
	            STATUS = :status,
	            UPDATED_DATE = :updatedDate
	        WHERE seq = :seq
		    """)
    Mono<Boolean> updateByContentUserSeq(
    	    @Param("seq") Integer seq,
    	    @Param("userName") String userName,
    	    @Param("userDept") String userDept,
    	    @Param("status") UserStatus status,
    	    @Param("updatedDate") LocalDateTime updatedDate
    );
    
    @Modifying
    @Query("""
	        UPDATE CONTENT_USER 
	        SET 
	            STATUS = :status,
	            deleted_date = :deletedDate
	        WHERE seq = :seq
		    """)
    Mono<Boolean> deleteByContentUserSeq(
    	    @Param("seq") Integer seq,
    	    @Param("status") UserStatus status,
    	    @Param("deletedDate") LocalDateTime deletedDate
    );
}