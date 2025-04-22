package com.ehas.center.scheduler;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
@Component
@RequiredArgsConstructor
@Slf4j
public class AppsScheduler {
	private final DiscoveryClient discoveryClient;  
	
	@Scheduled(cron = "0/1 * * * * ?")
    public void insertPerformanceMonitoring() {
		//log.info("START >>>> PerformanceMonitoring");
		//requestPerformanceMonitoring("http://127.0.0.1:8761/actuator/metrics/system.cpu.usage");

		List<String> services = this.discoveryClient.getServices();
		
		if(services.size() > 0) {
			log.debug("ing >>>> Services size :"+services.size());
		}
		for(String serviceName : services){
			List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(serviceName);
			
			if(serviceInstances.size() > 0) {
				log.debug("ing >>>> Service Name : ", serviceName ,", size : "+serviceInstances.size());
			}
			
			for(ServiceInstance serviceInstance : serviceInstances) {
				log.debug("ing >>>> Service Instance Name : ",serviceInstance.getInstanceId(),", Host : ", serviceInstance.getHost(),", Port :",serviceInstance.getPort());
				
				String url = serviceInstance.getUri().toString();
				String urlMaxJvmMemory  = String.format("$s/actuator/metrics/jvm.memory.max", url);
				String urlUsedJvmMemory  = String.format("$s/actuator/metrics/jvm.memory.used", url);
				String urlProcessCpuUsage  = String.format("$s/actuator/metrics/process.cpu.usage", url);
				String urlSystemCpuCount  = String.format("$s/actuator/metrics/system.cpu.count", url);
				String urlSystemCpuUsage  = String.format("$s/actuator/metrics/system.cpu.usage", url);
				
				requestPerformanceMonitoring(urlSystemCpuUsage,"System Cpu Usage");
			}
			
		}
    }
    
    private void requestPerformanceMonitoring(String url) {
		RequestEntity<Void> requestEntity = RequestEntity.get(url)
                										.accept(MediaType.APPLICATION_JSON).build();
		
		ParameterizedTypeReference<Map> responseType =	new ParameterizedTypeReference<>() {};
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity, responseType);
		
		Integer status = responseEntity.getStatusCodeValue();
		if(status == 200) {
			Map body = responseEntity.getBody();
			List<Map<String,Object>> measurements = (List<Map<String, Object>>) body.get("measurements");
			String value = measurements.get(0).get("value").toString();
			
			log.debug("ing >>>> Service Body : "+body.get("measurements"));
		}
    }
}
*/