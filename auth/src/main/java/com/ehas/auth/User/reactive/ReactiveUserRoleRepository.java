package com.ehas.auth.User.reactive;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.User.entity.UserRoleEntity;
import com.ehas.auth.User.entity.UserRoleEntityKey;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveUserRoleRepository extends R2dbcRepository<UserRoleEntity, UserRoleEntityKey>{
	@Query("SELECT * FROM user_role WHERE user_seq = :seq")
	Flux<UserRoleEntity> findByUserSeq(Integer seq);
	
	@Modifying
	@Query("""
			DELETE FROM user_role 
			WHERE user_seq = (SELECT seq 
								FROM user 
							   WHERE id = :userId)
			  AND role_seq = :roleSeq
			""")
	Mono<Boolean> findByUserIdAndRoleSeq(String userId, Integer roleSeq);
}
