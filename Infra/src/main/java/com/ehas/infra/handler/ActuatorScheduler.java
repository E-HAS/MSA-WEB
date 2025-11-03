package com.ehas.infra.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ehas.infra.Service.InstanceRegistryService;
import com.ehas.infra.Service.KafkaMetricsProducerService;
import com.ehas.infra.Service.ServerPrometheusService;
import com.ehas.infra.Service.ServerService;
import com.ehas.infra.Service.StompPrometheusService;
import com.ehas.infra.dto.PrometheusDto;
import com.ehas.infra.entity.ServerEntity;
import com.ehas.infra.entity.ServerPrometheusEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	private final StompPrometheusService stompPrometheusService;

	private static final Map<String, Integer> ServerMap = new ConcurrentHashMap<String, Integer>();
	private final ServerService serverService;

	private static final Map<String, Integer> ServerPrometheusMap = new ConcurrentHashMap<String, Integer>();
	private final ServerPrometheusService serverPrometheusService;
	
	@PostConstruct
	public void init() {
		// 1. 서버 서비스 목록 초기화
        List<ServerEntity> servers = serverService.findAll();
        for (ServerEntity server : servers) {
            String key = server.getName() + "|" + server.getHost();
            ServerMap.put(key, server.getSeq());
        }
        
		// 2. 서버 서비스 모니터링 옵션 목록 초기화
        List<ServerPrometheusEntity> serverPrometheuses = serverPrometheusService.findAll();
        for (ServerPrometheusEntity serverPrometheuse : serverPrometheuses) {
            String key = serverPrometheuse.getLabel()+serverPrometheuse.getOpt();
            ServerPrometheusMap.put(key, serverPrometheuse.getSeq());
        }
	}
	
	@Scheduled(cron = "0/1 * * * * ?")
    public void scheduledPerformanceMonitoringForSecond() throws JsonProcessingException {
		// 1. Eureka Client 서비스 목록 가져오기
		List<String> services = instanceRegistryService.getServices();
		
		for(String serviceName : services) {
			//2. Eureka Client 서비스에 등록된 서버들 가져오기
			Map<String, List> instances = instanceRegistryService.onPrometheusByService(serviceName);
			
			instances.keySet().parallelStream().forEach(instanceName -> {
				try {
					//Eureka Client 서비스 DB 등록 또는 캐싱(MAP)
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
					
					//Eureka Client 서비스 인스턴스에 대한 모니터링 정보 가져오기
					List<PrometheusDto> prometheusList = (List<PrometheusDto>) instances.get(instanceName);
					if(prometheusList.size() < 1) {
						return;
					}

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
					});
					 //log.info("stomp server : "+serviceName+", Seq"+serverSeq);
					 stompPrometheusService.onMessagePrometheus(serverSeq, 1, prometheusList);
					 //log.info("kafka server : "+serviceName+", Seq"+serverSeq);
					 kafkaMetricsProducerService.sendMessage(serverSeq, 1, prometheusList);
				}catch(Exception e){
					log.error("[ERROR] PerformanceMonitoring in instances error : "+e.getMessage());
				}
			});
		}
    }
	
	@Scheduled(cron = "0/60 * * * * ?")
    public void scheduledPerformanceMonitoringForMinute() throws JsonProcessingException {
		LocalDateTime dateNow = LocalDateTime.now();
		
		serverPrometheusService.insertPrometheusStatsFor1Min(dateNow.minusSeconds(60).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
															, dateNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}