package com.ehas.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ehas.infra.entity.ServerPrometheusEntity;

import jakarta.transaction.Transactional;

public interface ServerPrometheusJpaRepository extends JpaRepository<ServerPrometheusEntity, Integer>{
	@Modifying
	@Transactional
	@Query(value = """
	    INSERT INTO SERVER_PROMETHEUS_STAT(SERVER_SEQ, SERVER_PROMETHEUS_SEQ, VALUE, SECOND, REG_DATE)
	    SELECT SERVER_SEQ, SERVER_PROMETHEUS_SEQ, AVG(VALUE) as VALUE, 60 as SECOND, :enDt
	    FROM SERVER_PROMETHEUS_STAT
	    WHERE REG_DATE BETWEEN :stDt AND :enDt
	      AND SECOND = 1
	    GROUP BY SERVER_SEQ, SERVER_PROMETHEUS_SEQ
	    """, nativeQuery = true)
	void insertPrometheusStatsFor1Min(
	    @Param("stDt") String stDt,
	    @Param("enDt") String enDt
	);
}
