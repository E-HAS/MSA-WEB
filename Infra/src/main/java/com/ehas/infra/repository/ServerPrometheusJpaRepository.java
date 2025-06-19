package com.ehas.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehas.infra.entity.ServerPrometheusEntity;

public interface ServerPrometheusJpaRepository extends JpaRepository<ServerPrometheusEntity, Integer>{

}
