package com.ehas.Log.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.ehas.Log.entity.ServerPrometheusLogEntity;

public interface ServerPrometheusLogJpaRepository extends ReactiveCrudRepository<ServerPrometheusLogEntity, Integer>{

}
