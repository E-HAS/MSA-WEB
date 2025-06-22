package com.ehas.infra.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.infra.Service.ServerService;
import com.ehas.infra.dto.ServerEntityDto;
import com.ehas.infra.entity.ServerEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/servers")
@RequiredArgsConstructor
public class ServerRestController {

    private final ServerService serverService;

    @PostMapping
    public ResponseEntity<ServerEntity> create(@RequestBody ServerEntityDto dto) {
        return ResponseEntity.ok(serverService.create(ServerEntity.builder()
        															.name(dto.getName())
        															.host(dto.getHost())
        															.dept(dto.getDept())
        															.regDate(LocalDateTime.now())
        														  .build()));
    }

    @GetMapping
    public ResponseEntity<List<ServerEntity>> getAll() {
        return ResponseEntity.ok(serverService.findAll());
    }

    @GetMapping("/{seq}")
    public ResponseEntity<ServerEntity> getById(@PathVariable Integer seq) {
        return ResponseEntity.ok(serverService.findById(seq));
    }

    @PutMapping("/{seq}")
    public ResponseEntity<ServerEntity> update(@PathVariable Integer seq, @RequestBody ServerEntityDto dto) {
        return ResponseEntity.ok(serverService.update(seq, ServerEntity.builder()
																		.name(dto.getName())
																		.host(dto.getHost())
																		.dept(dto.getDept())
																	  .build()));
    }

    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> delete(@PathVariable Integer seq) {
        serverService.delete(seq);
        return ResponseEntity.noContent().build();
    }
}