package com.ehas.auth.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.entity.UserRole;
import com.ehas.auth.entity.UserRoleEntity;
import com.ehas.auth.entity.UserRoleEntityKey;
import com.ehas.auth.entity.UserRoleKey;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveUserRoleRepository extends R2dbcRepository<UserRole, UserRoleKey>{
	@Query("SELECT * FROM userrole WHERE user_id = :id")
	Flux<UserRoleEntity> findUserRoleByRxUserId(String id);
	
	@Query("SELECT * FROM userrole WHERE user_uid = :uid")
	Flux<UserRoleEntity> findUserRoleByRxUid(Integer uid);
}
