package com.ehas.infra.handler;

import java.util.List;
import java.util.Map;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class ActuatorScheduler {
	private final InstanceRegistryService instanceRegistryService;
	
	@Scheduled(cron = "0/1 * * * * ?")
    public void insertPerformanceMonitoring() {
		List<String> services = instanceRegistryService.getServices();
		
		for(String serviceName : services) {
			//log.info("METRICS SERVICE NAME : "+serviceName);
			Map<String, Map> instances = instanceRegistryService.onMetricsByService(serviceName);
			
			for(String instanceName : instances.keySet()) {
				//log.info("METRICS INSTANCE NAME : "+serviceName);
				//log.info("METRICS INSTANCE PERFORMANCE : "+instances.get(instanceName));
			}
		}
    }
}