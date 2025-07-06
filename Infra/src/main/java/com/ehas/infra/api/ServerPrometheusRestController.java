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

import com.ehas.infra.Service.ServerPrometheusService;
import com.ehas.infra.dto.ServerPrometheusEntityDto;
import com.ehas.infra.entity.ServerPrometheusEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/server-prometheus")
@RequiredArgsConstructor
public class ServerPrometheusRestController {

    private final ServerPrometheusService serverPrometheusService;

    @PostMapping
    public ResponseEntity<ServerPrometheusEntity> create(@RequestBody ServerPrometheusEntityDto dto) {
        return ResponseEntity.ok(serverPrometheusService.create(ServerPrometheusEntity.builder()
        																.label(dto.getLabel())
        																.opt(dto.getOpt())
        																.dept(dto.getDept())
        																.regDate(LocalDateTime.now())
        																.build()));
    }

    @GetMapping
    public ResponseEntity<List<ServerPrometheusEntity>> getAll() {
        return ResponseEntity.ok(serverPrometheusService.findAll());
    }

    @GetMapping("/{seq}")
    public ResponseEntity<ServerPrometheusEntity> getById(@PathVariable Integer seq) {
        return ResponseEntity.ok(serverPrometheusService.findById(seq));
    }

    @PutMapping("/{seq}")
    public ResponseEntity<ServerPrometheusEntity> update(@PathVariable Integer seq, @RequestBody ServerPrometheusEntityDto dto) {
        return ResponseEntity.ok(serverPrometheusService.update(seq, ServerPrometheusEntity.builder()
																			.label(dto.getLabel())
																			.opt(dto.getOpt())
																			.dept(dto.getDept())
																			.build()));
    }

    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> delete(@PathVariable Integer seq) {
    	serverPrometheusService.delete(seq);
        return ResponseEntity.noContent().build();
    }
}