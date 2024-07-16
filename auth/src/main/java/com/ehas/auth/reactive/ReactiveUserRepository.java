package com.ehas.auth.reactive;

import java.util.Optional;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.ehas.auth.entity.User;

import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUserRepository extends R2dbcRepository<User, String>{
	@Query("SELECT * FROM USER WHERE uid = :uid")
	Mono<User> findByIdRx(String uid);
	
	@Query("SELECT * FROM USER WHERE userId = :userId")
	Optional<User> findByUserId(String userId);
}
