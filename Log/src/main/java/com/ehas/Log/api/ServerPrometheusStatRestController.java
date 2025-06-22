package com.ehas.Log.api;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import com.ehas.Log.dto.ServerPrometheusStatDto;
import com.ehas.Log.entity.ServerPrometheusStatEntity;
import com.ehas.Log.service.ServerPrometheusStatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/server-prometheus-log")
@RequiredArgsConstructor
public class ServerPrometheusStatRestController {

    private final ServerPrometheusStatService serverPrometheusStatService;

    @GetMapping
    public Flux<ServerPrometheusStatEntity> getAllLogs() {
        return serverPrometheusStatService.findAll();
    }
    @PostMapping
    public Mono<ServerPrometheusStatEntity> addLog(@RequestBody ServerPrometheusStatDto dto) {
        return serverPrometheusStatService.add(ServerPrometheusStatEntity.builder()
																		.regDate(LocalDateTime.now())
																		.second(dto.getSecond())
																		.serverSeq(dto.getServerSeq())
																		.serverPrometheusSeq(dto.getServerPrometheusSeq())
																		.value(dto.getValue())
																		.build());
    }
    
    @GetMapping("/{seq}")
    public Mono<ServerPrometheusStatEntity> getLogById(
            @PathVariable("seq") Integer seq) {
        return serverPrometheusStatService.findById(seq);
    }
    
    @DeleteMapping("/{seq}")
    public Mono<Void> deleteLog(@PathVariable("seq") Integer seq) {
        return serverPrometheusStatService.deleteById(seq);
    }
}
