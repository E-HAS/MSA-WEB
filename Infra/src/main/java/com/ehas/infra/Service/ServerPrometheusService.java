package com.ehas.infra.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ehas.infra.entity.ServerPrometheusEntity;
import com.ehas.infra.repository.ServerPrometheusJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServerPrometheusService {

    private final ServerPrometheusJpaRepository serverPrometheusJpaRepository;

    public ServerPrometheusEntity create(ServerPrometheusEntity entity) {
        return serverPrometheusJpaRepository.save(entity);
    }

    public List<ServerPrometheusEntity> findAll() {
        return serverPrometheusJpaRepository.findAll();
    }

    public ServerPrometheusEntity findById(Integer seq) {
        return serverPrometheusJpaRepository.findById(seq).orElse(null);
    }

    public ServerPrometheusEntity update(Integer seq, ServerPrometheusEntity updatedData) {
        ServerPrometheusEntity entity = findById(seq);
        entity.setLabel(updatedData.getLabel());
        entity.setOpt(updatedData.getOpt());
        entity.setDept(entity.getDept());
        return serverPrometheusJpaRepository.save(entity);
    }

    public void delete(Integer seq) {
    	serverPrometheusJpaRepository.deleteById(seq);
    }
}