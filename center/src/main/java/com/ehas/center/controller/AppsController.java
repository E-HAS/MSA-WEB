package com.ehas.center.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/app/")
@RequiredArgsConstructor
public class AppsController {
	private DiscoveryClient discoveryClient;  
	
	@GetMapping("/ping/{service}")  
	public List<ServiceInstance> ping(
			@PathVariable("service") String serviceName) {  
		//List<String> services = discoveryClient.getServices();
		//log.info("FIND SERVICES : count = {}", services.size());  
		
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);  
		log.info("FIND INSTANCES : count = {}", instances.size());  
		instances.forEach(it -> log.info("FIND INSTANCE EACH : id={}, port={}", it.getServiceId(), it.getPort()));  
		
		return instances;  
	}
	
	@GetMapping("/all/monitoring")
	public ResponseEntity AppsPerformanceMonitoring() {
		List<String> services = this.discoveryClient.getServices();
		if(services.size() > 0) {
			log.debug("ing >>>> Services size :"+services.size());
		}
		
		
		Map<String, Map> serviceInfo = new HashMap<String, Map>();
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
				
				serviceInfo.put(serviceInstance.getInstanceId()
										,Map.of("jvm.memory.max", requestPerformanceMonitoring(urlMaxJvmMemory)
									    ,"jvm.memory.used",requestPerformanceMonitoring(urlUsedJvmMemory)
									    ,"process.cpu.usage",requestPerformanceMonitoring(urlProcessCpuUsage)
									    ,"system.cpu.count",requestPerformanceMonitoring(urlSystemCpuCount)
									    ,"system.cpu.usage",requestPerformanceMonitoring(urlSystemCpuUsage)));
			}
		}
		
		return ResponseEntity.ok(serviceInfo);
	}
	
	
	 private String requestPerformanceMonitoring(String url) {
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
				
				return value;
			}
			return "";
	    }
	
}
