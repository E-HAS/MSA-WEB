package com.ehas.Log.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ehas.Log.entity.ServerPrometheusLogEntity;
import com.ehas.Log.repository.ServerPrometheusLogJpaRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ServerPrometheusLogService {

    private final ServerPrometheusLogJpaRepository serverPrometheusLogJpaRepository;

    public Mono<ServerPrometheusLogEntity> add(ServerPrometheusLogEntity entity) {
        return serverPrometheusLogJpaRepository.save(entity);
    }
    
    public Flux<ServerPrometheusLogEntity> addAll(List<ServerPrometheusLogEntity> entitys) {
        return serverPrometheusLogJpaRepository.saveAll(entitys);
    }
    
    
    public Mono<Void> deleteById(Integer seq) {
        return serverPrometheusLogJpaRepository.deleteById(seq);
    }

    public Flux<ServerPrometheusLogEntity> findAll() {
        return serverPrometheusLogJpaRepository.findAll();
    }

    public Mono<ServerPrometheusLogEntity> findById(Integer seq) {
        return serverPrometheusLogJpaRepository.findById(seq);
    }
}