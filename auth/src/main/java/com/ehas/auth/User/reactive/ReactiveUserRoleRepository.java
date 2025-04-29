package com.ehas.auth.User.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.User.entity.UserRoleEntity;
import com.ehas.auth.User.entity.UserRoleEntityKey;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveUserRoleRepository extends R2dbcRepository<UserRoleEntity, UserRoleEntityKey>{
	@Query("SELECT * FROM userrole WHERE user_seq = :seq")
	Flux<UserRoleEntity> findBySeq(Integer seq);
	
	@Query("SELECT * FROM userrole WHERE user_seq = :userSeq AND content_seq = : contentSeq")
	Flux<UserRoleEntity> findByUserSeqAndContentSeq(Integer userSeq, Integer contentSeq);
}
