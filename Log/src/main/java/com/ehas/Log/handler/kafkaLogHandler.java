package com.ehas.Log.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ehas.Log.dto.ConsumePrometheusDto;
import com.ehas.Log.entity.ServerPrometheusStatEntity;
import com.ehas.Log.service.ServerPrometheusStatService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class kafkaLogHandler {
	private final ServerPrometheusStatService serverPrometheusStatService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private final KafkaReceiver<String, String> kafkaReceiver;
    @PostConstruct
    public void init() {
        kafkaReceiver.receive()
                .flatMap(this::consumeHandle) 
                .doOnError(e -> log.error("KafkaReceiver init error: ", e))
                .subscribe(); 
    }
    
    private Mono<Void> consumeHandle(ReceiverRecord<String, String> record) {
        try {
        	String message = record.value();
            ConsumePrometheusDto dto = objectMapper.readValue(message, ConsumePrometheusDto.class);

            switch(dto.getSecond()) {
            	case 1:
            		List<ServerPrometheusStatEntity> list = dto.getList().stream()
                    .map(v -> ServerPrometheusStatEntity.builder()
                            .second(dto.getSecond())
                            .regDate(LocalDateTime.now())
                            .serverSeq(dto.getServerSeq())
                            .serverPrometheusSeq(v.getSeq())
                            .value(v.getValue())
                            .build())
                    .toList();

		            return serverPrometheusStatService.addAll(list)
							                    .doOnSuccess(v -> record.receiverOffset().acknowledge())
							                    .doOnError(e -> log.error("Error saving logs: {}", e.getMessage(), e))
							                    .then();
            }

        } catch (Exception e) {
            log.error("[Error] consumeHandle error : "+e.getStackTrace().toString());
            return Mono.empty(); // 메시지 처리 실패 → commit 안함
        }

        return Mono.empty();
    }
	/*
    @KafkaListener(
    		topics = "${value.kafka.metrics.topic.default-topic-name}"
    		, groupId = "${value.kafka.metrics.group-id}"
    		, containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String message) throws IOException {
    	ConsumePrometheusDto dto = objectMapper.readValue(message, ConsumePrometheusDto.class);
    	
    	switch(dto.getSecond()) {
    		case 1:
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
    			break;
    		case 60:
    			serverPrometheusLogService.statisticsSecond(dto.getStDt(), dto.getEnDt()).subscribe();
    			
    	}
    }
     */
}