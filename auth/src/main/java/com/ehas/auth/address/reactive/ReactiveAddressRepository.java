package com.ehas.auth.address.reactive;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ehas.auth.address.entity.AddressEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveAddressRepository extends R2dbcRepository<AddressEntity, Integer>{
	@Query(   "SELECT * "
			+ "FROM ADDRESS "
			+ "WHERE SEQ = :addressSeq")
	Mono<AddressEntity> findByAddressSeq(Integer addressSeq);
	
	@Query(   "SELECT * "
			+ "FROM ADDRESS "
			+ "WHERE sido_code = :sido "
			+ "And gugun_code = :gugun "
			+ "And dong_code = :dong "
			+ "And ri_code = :ri ")
	Flux<AddressEntity> findBySidoAndGugunAndDongAndRi(Integer sido, Integer gugun, Integer dong, Integer ri);
}
