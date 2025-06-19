package com.ehas.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehas.infra.entity.ServerEntity;


public interface ServerJpaRepository extends JpaRepository<ServerEntity, Integer>{

}
