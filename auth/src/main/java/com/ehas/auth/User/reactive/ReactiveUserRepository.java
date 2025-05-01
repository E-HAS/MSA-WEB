package com.ehas.auth.User.reactive;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehas.auth.User.entity.UserEntity;
import com.ehas.auth.User.userstatus.UserStatus;

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
	
    @Modifying
    @Query("""
	        UPDATE user 
	        SET 
	            name = :name,
	            password = :password,
	            status = :status,
	            address_seq = :addressSeq,
	            password_updated_date = :passwordUpdatedDate,
	            updated_date = :updatedDate,
	            deleted_date = :deletedDate
	        WHERE seq = :seq
		    """)
    Mono<Boolean> updateUserBySeq(
    	    @Param("seq") Integer seq,
    	    @Param("name") String name,
    	    @Param("password") String password,
    	    @Param("status") UserStatus status,
    	    @Param("addressSeq") Integer addressSeq,
    	    @Param("passwordUpdatedDate") LocalDateTime passwordUpdatedDate,
    	    @Param("updatedDate") LocalDateTime updatedDate,
    	    @Param("deletedDate") LocalDateTime deletedDate
    );
}
