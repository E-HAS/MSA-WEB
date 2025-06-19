package com.ehas.infra.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ehas.infra.Service.InstanceRegistryService;
import com.ehas.infra.Service.KafkaMetricsProducerService;
import com.ehas.infra.Service.ServerPrometheusService;
import com.ehas.infra.Service.ServerService;
import com.ehas.infra.dto.prometheusDto;
import com.ehas.infra.entity.ServerEntity;
import com.ehas.infra.entity.ServerPrometheusEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class ActuatorScheduler {
	private final InstanceRegistryService instanceRegistryService;
	private final KafkaMetricsProducerService kafkaMetricsProducerService;
    
	private static final Map<String, Integer> ServerMap = new ConcurrentHashMap<String, Integer>();
	private final ServerService serverService;
	
	private static final Map<String, Integer> ServerPrometheusMap = new ConcurrentHashMap<String, Integer>();
	private final ServerPrometheusService serverPrometheusService;
	
	 private final ObjectMapper objectMapper;
	
	@PostConstruct
	public void init() {
        List<ServerEntity> servers = serverService.findAll();
        for (ServerEntity server : servers) {
            String key = server.getName() + "|" + server.getHost();
            ServerMap.put(key, server.getSeq());
        }
        
        List<ServerPrometheusEntity> serverPrometheuses = serverPrometheusService.findAll();
        for (ServerPrometheusEntity serverPrometheuse : serverPrometheuses) {
            String key = serverPrometheuse.getLabel()+serverPrometheuse.getOpt();
            ServerPrometheusMap.put(key, serverPrometheuse.getSeq());
        }
	}
	
	@Scheduled(cron = "0/1 * * * * ?")
    public void scheduledPerformanceMonitoring() throws JsonProcessingException {
		List<String> services = instanceRegistryService.getServices();
		
		for(String serviceName : services) {
			Map<String, List> instances = instanceRegistryService.onPrometheusByService(serviceName);
			
			instances.keySet().parallelStream().forEach(instanceName -> {
				try {
					//서버 관련
					int serverSeq = ServerMap.computeIfAbsent(instanceName, key -> {
					    String[] strSplits = key.split("\\|");
					    ServerEntity created = serverService.create(
					        ServerEntity.builder()
					            .name(strSplits[0])
					            .host(strSplits[1])
					            .regDate(LocalDateTime.now())
					            .build()
					    );
					    return created.getSeq();
					});
					
					//라벨 관련
					List<prometheusDto> prometheusList = (List<prometheusDto>) instances.get(instanceName);
					 prometheusList.parallelStream().forEach(PrometheusDto -> {
							String serverPrometheus = PrometheusDto.getLabel()+PrometheusDto.getOpt();
							
							int serverPrometheusSeq = ServerPrometheusMap.computeIfAbsent(serverPrometheus, key -> {
							    ServerPrometheusEntity created = serverPrometheusService.create(
							        ServerPrometheusEntity.builder()
							            .label(PrometheusDto.getLabel())
							            .opt(PrometheusDto.getOpt())
							            .regDate(LocalDateTime.now())
							            .build()
							    );
							    return created.getSeq();
							});
							
							PrometheusDto.setSeq(serverPrometheusSeq);
							PrometheusDto.setText(null);
							PrometheusDto.setLabel(null);
							PrometheusDto.setOpt(null);
					 });
					 String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Map.of("serverSeq",serverSeq
							 																				,"second",1
																											,"list", instances.get(instanceName)));
					 JsonNode jsonNode = objectMapper.readTree(json);
					 String changeJson = objectMapper.writeValueAsString(jsonNode);
					 
					 kafkaMetricsProducerService.sendMessage(changeJson);
				}catch(Exception e){
					log.error("[Error] PerformanceMonitoring in instances error : "+e.getMessage());
				}
			});
		}
    }
}