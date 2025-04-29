package com.ehas.auth.content.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.content.entity.RoleEntity;

import reactor.core.publisher.Flux;

public interface ReactiveRoleRepository extends R2dbcRepository<RoleEntity, Integer>{
	@Query(   "SELECT r.* "
			+ "FROM USERROLE as ur "
			+ "LEFT OUTER JOIN ROLE as r "
			+ "			 ON ur.role_seq = r.seq "
			+ "WHERE ur.user_seq = :seq ")
	Flux<RoleEntity> findByUserSeq(Integer seq);
}
