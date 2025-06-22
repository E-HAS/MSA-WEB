package com.ehas.Log.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.ehas.Log.entity.ServerPrometheusStatEntity;

public interface ServerPrometheusStatJpaRepository extends ReactiveCrudRepository<ServerPrometheusStatEntity, Integer>{

	
}
