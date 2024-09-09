package com.ehas.center.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/apps/api")
@RequiredArgsConstructor
public class AppsController {
	private DiscoveryClient discoveryClient;  
	
	@GetMapping("/ping/{service}")  
	public List<ServiceInstance> ping(
			@PathVariable("service") String serviceName) {  
		List<String> services = discoveryClient.getServices();
		log.info("FIND SERVICES : count = {}", services.size());  
		
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);  
		log.info("FIND INSTANCES : count = {}", instances.size());  
		instances.forEach(it -> log.info("FIND INSTANCE EACH : id={}, port={}", it.getServiceId(), it.getPort()));  
		
		return instances;  
	}
	
}
