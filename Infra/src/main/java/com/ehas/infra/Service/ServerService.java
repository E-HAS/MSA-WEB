package com.ehas.infra.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ehas.infra.entity.ServerEntity;
import com.ehas.infra.repository.ServerJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerJpaRepository serverJpaRepository;

    public ServerEntity create(ServerEntity serverEntity) {
        return serverJpaRepository.save(serverEntity);
    }

    public List<ServerEntity> findAll() {
        return serverJpaRepository.findAll();
    }

    public ServerEntity findById(Integer seq) {
        return serverJpaRepository.findById(seq).orElse(null);
    }

    public ServerEntity update(Integer seq, ServerEntity updatedData) {
        ServerEntity server = findById(seq);
        server.setName(updatedData.getName());
        server.setHost(updatedData.getHost());
        server.setDept(updatedData.getDept());
        return serverJpaRepository.save(server);
    }
    
    public void delete(Integer seq) {
    	serverJpaRepository.deleteById(seq);
    }
}