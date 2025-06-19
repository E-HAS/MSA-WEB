package com.ehas.Log.api;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import com.ehas.Log.entity.ServerPrometheusLogEntity;
import com.ehas.Log.service.ServerPrometheusLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class ServerPrometheusLogRestController {

    private final ServerPrometheusLogService serverPrometheusLogService;

    // 전체 로그 조회
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ServerPrometheusLogEntity> getAllLogs() {
        return serverPrometheusLogService.findAll();
    }

    // 단일 로그 조회
    @GetMapping("/{seq}")
    public Mono<ServerPrometheusLogEntity> getLogById(
            @PathVariable("seq") Integer seq) {
        return serverPrometheusLogService.findById(seq);
    }

    // 로그 저장
    @PostMapping
    public Mono<ServerPrometheusLogEntity> createLog(@RequestBody ServerPrometheusLogEntity log) {
        return serverPrometheusLogService.add(log);
    }

    // 로그 삭제
    @DeleteMapping("/{seq}")
    public Mono<Void> deleteLog(
    		@PathVariable("seq") Integer seq) {
        return serverPrometheusLogService.deleteById(seq);
    }
}
