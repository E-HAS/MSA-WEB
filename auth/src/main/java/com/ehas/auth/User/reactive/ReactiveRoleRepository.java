package com.ehas.auth.User.reactive;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.ehas.auth.User.entity.RoleEntity;
import com.ehas.auth.User.userstatus.UserStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveRoleRepository extends R2dbcRepository<RoleEntity, Integer>{
	@Query(   "SELECT r.* "
			+ "FROM USER_ROLE as ur "
			+ "LEFT OUTER JOIN ROLE as r "
			+ "			 ON ur.role_seq = r.seq "
			+ "WHERE ur.user_seq = :seq ")
	Flux<RoleEntity> findByUserSeq(Integer seq);
	
	@Query(   "SELECT r.* "
			+ "FROM User as u "
			+ "LEFT OUTER JOIN USER_ROLE as ur "
			+ "			 ON u.seq = ur.user_seq "
			+ "LEFT OUTER JOIN ROLE as r "
			+ "			 ON ur.role_seq = r.seq "
			+ "WHERE u.id = :id ")
	Flux<RoleEntity> findByUserId(String id);
	
    @Modifying
    @Query("""
	        UPDATE role 
	        SET 
	            role_name = :roleName,
	            role_dept = :roleDept
	        WHERE seq = :seq
		    """)
    Mono<Boolean> updateByseq(
    		@Param("seq") Integer seq,
    	    @Param("roleName") String roleName,
    	    @Param("roleDept") String roleDept
    );
}
