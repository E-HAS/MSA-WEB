package com.ehas.center.restController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@RequestMapping("/api")
@RequiredArgsConstructor
public class serviceRestController {
	private final DiscoveryClient discoveryClient;  
	
	@GetMapping("/services")  
	public Map<String, List<String>> services() {
		List<String> services = discoveryClient.getServices();
		return Map.of("service",services);
	}
	
	@GetMapping("/services/{serviceName}")  
	public Map<String, List<String>> serviceByServiceName(@PathVariable("serviceName") String _serviceName) {
		List<ServiceInstance> instances = discoveryClient.getInstances(_serviceName);
		return Map.of("insetance", instances.stream().map(v-> v.getServiceId()+":"+v.getPort()).collect(Collectors.toList()));
	}  
	
	
	@GetMapping("/refresh/{serviceName}")  
	public ResponseEntity refreshByserviceName(@PathVariable("serviceName") String _serviceName) {
		List<ServiceInstance> instances = discoveryClient.getInstances(_serviceName);
		List<String> instancesList = new ArrayList<String>();
		for(ServiceInstance instance : instances) {
			String url = instance.getUri().toString();
			instancesList.add( requestRefresh(String.format("$s/actuator/refresh", url)) );
		}
		
		return ResponseEntity.ok(instancesList);
	}
	
	@GetMapping("/monitorig/{serviceName}")  
	public ResponseEntity monitorigByserviceName(@PathVariable("serviceName") String _serviceName) {
		List<ServiceInstance> instances = discoveryClient.getInstances(_serviceName);
		
		Map<String, Map> instancesList = new HashMap<String, Map>();
		for(ServiceInstance instance : instances) {
			String url = instance.getUri().toString();
			String urlMaxJvmMemory  = String.format("$s/actuator/metrics/jvm.memory.max", url);
			String urlUsedJvmMemory  = String.format("$s/actuator/metrics/jvm.memory.used", url);
			String urlProcessCpuUsage  = String.format("$s/actuator/metrics/process.cpu.usage", url);
			String urlSystemCpuCount  = String.format("$s/actuator/metrics/system.cpu.count", url);
			String urlSystemCpuUsage  = String.format("$s/actuator/metrics/system.cpu.usage", url);
			
			instancesList.put(instance.getInstanceId()
					,Map.of("jvm.memory.max", requestPerformanceMonitoring(urlMaxJvmMemory)
				    ,"jvm.memory.used",requestPerformanceMonitoring(urlUsedJvmMemory)
				    ,"process.cpu.usage",requestPerformanceMonitoring(urlProcessCpuUsage)
				    ,"system.cpu.count",requestPerformanceMonitoring(urlSystemCpuCount)
				    ,"system.cpu.usage",requestPerformanceMonitoring(urlSystemCpuUsage)));
		}
		
		return ResponseEntity.ok(instancesList);
	}
	
	
	private String requestRefresh(String url) {
		RestTemplate restTemplate = new RestTemplate();
		
	 	RequestEntity<Void> requestEntity = RequestEntity.post(url)
	 													 .accept(MediaType.APPLICATION_JSON)
	 													 .build();
        ResponseEntity<String> response = restTemplate.exchange(requestEntity,String.class);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            return "Refresh Success";
        } else {
            return "Refresh fail";
        }
	}

	private String requestPerformanceMonitoring(String url) {

		RestTemplate restTemplate = new RestTemplate();

		RequestEntity<Void> requestEntity = RequestEntity.get(url)
														 .accept(MediaType.APPLICATION_JSON)
														 .build();
		ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<Map<String, Object>>() {
				});

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			Map<String, Object> body = responseEntity.getBody();
			if (body != null) {
				if (body.containsKey("measurements")) {
					List<Map<String, Object>> measurements = (List<Map<String, Object>>) body.get("measurements");
					if (measurements != null && !measurements.isEmpty()) {
						return measurements.get(0).get("value").toString();
					}
				}
			}
		}
		return "";
	}
}
