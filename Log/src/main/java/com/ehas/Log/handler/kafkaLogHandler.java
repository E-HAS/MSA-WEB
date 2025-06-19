package com.ehas.Log.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ehas.Log.dto.ConsumePrometheusDto;
import com.ehas.Log.entity.ServerPrometheusLogEntity;
import com.ehas.Log.service.ServerPrometheusLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class kafkaLogHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ServerPrometheusLogService serverPrometheusLogService;
    @KafkaListener(
    		topics = "${value.kafka.metrics.topic.default-topic-name}"
    		, groupId = "${value.kafka.metrics.group-id}"
    		, containerFactory = "kafkaListenerContainerFactory"
    )
    
    public void consume(String message) throws IOException {
    	ConsumePrometheusDto dto = objectMapper.readValue(message, ConsumePrometheusDto.class);
    	
    	List<ServerPrometheusLogEntity> lists = new ArrayList<ServerPrometheusLogEntity>();
    	dto.getList().forEach(v->{
    		lists.add(ServerPrometheusLogEntity.builder()
    											.second(dto.getSecond())
    											.regDate(LocalDateTime.now())
    											.serverSeq(dto.getServerSeq())
    											.serverPrometheusSeq(v.getSeq())
    											.value(v.getValue())
    											.build());
    	});
    	
		serverPrometheusLogService.addAll(lists)
									.doOnError(e -> log.error("[Error] Server Prometheus Log Saved"+e.getMessage()))
									.subscribe();
    }
}