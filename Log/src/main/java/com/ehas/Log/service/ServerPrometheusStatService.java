package com.ehas.Log.service;
import java.util.List;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import com.ehas.Log.entity.ServerPrometheusStatEntity;
import com.ehas.Log.repository.ServerPrometheusStatJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerPrometheusStatService {
    private final ServerPrometheusStatJpaRepository serverPrometheusStatJpaRepository;
    private final DatabaseClient databaseClient;
    
    public Mono<ServerPrometheusStatEntity> add(ServerPrometheusStatEntity entity) {
        return serverPrometheusStatJpaRepository.save(entity);
    }
    
    public Mono<Long> addAll(List<ServerPrometheusStatEntity> entities) {
    	if (entities == null || entities.isEmpty()) {
            return Mono.empty();
        }

        StringBuilder sql = new StringBuilder("INSERT INTO server_prometheus_log ");
        sql.append("(reg_date, second, server_seq, server_prometheus_seq, value) VALUES ");

        for (int i = 0; i < entities.size(); i++) {
            sql.append("(:regDate_").append(i)
               .append(", :second_").append(i)
               .append(", :serverSeq_").append(i)
               .append(", :serverPrometheusSeq_").append(i)
               .append(", :value_").append(i).append(")");
            if (i < entities.size() - 1) {
                sql.append(", ");
            }
        }

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sql.toString());

        for (int i = 0; i < entities.size(); i++) {
        	ServerPrometheusStatEntity e = entities.get(i);
            spec = spec.bind("regDate_" + i, e.getRegDate())
                       .bind("second_" + i, e.getSecond())
                       .bind("serverSeq_" + i, e.getServerSeq())
                       .bind("serverPrometheusSeq_" + i, e.getServerPrometheusSeq())
                       .bind("value_" + i, e.getValue());
        }

        return spec.fetch().rowsUpdated();
    }
    
    
    public Mono<Void> deleteById(Integer seq) {
        return serverPrometheusStatJpaRepository.deleteById(seq);
    }

    public Flux<ServerPrometheusStatEntity> findAll() {
        return serverPrometheusStatJpaRepository.findAll();
    }

    public Mono<ServerPrometheusStatEntity> findById(Integer seq) {
        return serverPrometheusStatJpaRepository.findById(seq);
    }
}