package com.ehas.Log.service;
import java.util.List;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import com.ehas.Log.entity.ServerPrometheusLogEntity;
import com.ehas.Log.repository.ServerPrometheusLogJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerPrometheusLogService {
    private final ServerPrometheusLogJpaRepository serverPrometheusLogJpaRepository;
    private final DatabaseClient databaseClient;
    
    public Mono<ServerPrometheusLogEntity> add(ServerPrometheusLogEntity entity) {
        return serverPrometheusLogJpaRepository.save(entity);
    }
    
    public Mono<Long> addAll(List<ServerPrometheusLogEntity> entities) {
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
            ServerPrometheusLogEntity e = entities.get(i);
            spec = spec.bind("regDate_" + i, e.getRegDate())
                       .bind("second_" + i, e.getSecond())
                       .bind("serverSeq_" + i, e.getServerSeq())
                       .bind("serverPrometheusSeq_" + i, e.getServerPrometheusSeq())
                       .bind("value_" + i, e.getValue());
        }

        return spec.fetch().rowsUpdated();
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
    
    public Mono<Void> statisticsSecond(String stDt,  String enDt) {
        return databaseClient.sql("""
        		INSERT INTO SERVER_PROMETHEUS_LOG(SERVER_SEQ,SERVER_PROMETHEUS_SEQ, VALUE, SECOND, REG_DATE)
				SELECT SERVER_SEQ, SERVER_PROMETHEUS_SEQ, AVG(VALUE) as VALUE, 60 as SECOND, :enDt
				FROM SERVER_PROMETHEUS_LOG
				WHERE REG_DATE BETWEEN :stDt AND :enDt
				  AND SECOND=1
				GROUP BY SERVER_SEQ, SERVER_PROMETHEUS_SEQ;
        		""")
            .bind("stDt", stDt)
            .bind("enDt", enDt)
            .then()
            .doOnSuccess(v->log.info("[Successed] statisticsSecond: 60 stDt:"+stDt+", enDt:"+enDt))
            .doOnError(e-> log.info("[Failed] statisticsSecond: 60 stDt:"+stDt+", enDt:"+enDt+" error : "+e.getStackTrace().toString()));
    }
}