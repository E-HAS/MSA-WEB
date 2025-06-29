package com.ehas.infra.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ehas.infra.dto.PrometheusDto;
import com.ehas.infra.dto.PrometheusMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StompPrometheusService{
	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;
	  
	@Async
	public void onMessagePrometheus(Integer serverSeq, Integer second, List<PrometheusDto> dtos){
		 
		try {
			String json = objectMapper.writeValueAsString(PrometheusMessageDto.builder()
																				.time(LocalDateTime.now()
																					.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
																				.serverSeq(serverSeq)
																				.second(1)
																				.lists(dtos)
																				.build());
			
			messagingTemplate.convertAndSend("/topic/monitoring/"+serverSeq, json);
			
		} catch (JsonProcessingException e) {
			log.error("[ERROR] Send Stomp Prometheus serverSeq : " +serverSeq+", second : "+second+", message : "+e.getMessage() );
		}
	}
}
