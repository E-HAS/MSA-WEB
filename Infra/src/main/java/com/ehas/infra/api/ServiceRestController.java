package com.ehas.infra.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.ehas.infra.Service.InstanceRegistryService;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceRestController {
	private final InstanceRegistryService instanceRegistryService;
	
	@GetMapping("/services")  
	public Map<String, List<String>> getServices() {
        List<String> services = instanceRegistryService.getServices();
		return Map.of("services", services);
	}
	
	@GetMapping("/services/{serviceName}")  
	public Map<String, List<String>> getInstanceIdsByService(@PathVariable("serviceName") String _serviceName) {
        List<String> instances = instanceRegistryService.getInstanceIdsByService(_serviceName);
		return Map.of("instances", instances);
	}  
	
	
	@GetMapping("/services/{serviceName}/refresh")  
	public ResponseEntity onRefreshByService(@PathVariable("serviceName") String _serviceName) {
        List<String> instancesList = instanceRegistryService.onRefreshByService(_serviceName);

        return ResponseEntity.ok(instancesList);
	}
	
	@GetMapping("/services/{serviceName}/monitoring")  
	public ResponseEntity monitorigByserviceName(@PathVariable("serviceName") String _serviceName) {
		Map<String, Map> instancesList =instanceRegistryService.onMetricsByService(_serviceName);

        return ResponseEntity.ok(instancesList);
	}
}
