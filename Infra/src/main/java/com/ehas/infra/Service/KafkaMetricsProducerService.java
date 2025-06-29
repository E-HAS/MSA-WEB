package com.ehas.infra.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ehas.infra.dto.PrometheusDto;
import com.ehas.infra.dto.PrometheusMessageDto;
import com.ehas.infra.handler.ActuatorScheduler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaMetricsProducerService {

	@Value("${value.kafka.metrics.topic.services-metrics-name}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaMetricsProducerService(KafkaTemplate<String,String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Async
    public void sendMessage(Integer serverSeq, Integer second, List<PrometheusDto> dtos) {
		try {
			List<PrometheusDto> filterDtos = dtos.stream()
					.map(v->{
						return PrometheusDto.builder()
											.seq(v.getSeq())
											.value(v.getValue())
											.build();
					})
				    .collect(Collectors.toList());
			
			String json = objectMapper.writeValueAsString(PrometheusMessageDto.builder()
																.serverSeq(serverSeq)
																.second(1)
																.lists(filterDtos)
																.build());
			this.kafkaTemplate.send(topicName, json);
		} catch (JsonProcessingException e) {
			log.error("[ERROR] send topic name : "+topicName + " serverSeq : " +serverSeq+" message : "+e.getMessage() );
		}
    
    }
}